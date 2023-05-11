package smu.poodle.smnavi.map.odsay;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.map.domain.Edge;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.domain.Station;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.map.dto.DetailPositionDto;
import smu.poodle.smnavi.map.dto.StationDto;
import smu.poodle.smnavi.map.dto.TransitPathDto;
import smu.poodle.smnavi.map.dto.TransitSubPathDto;
import smu.poodle.smnavi.map.externapi.*;
import smu.poodle.smnavi.map.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static smu.poodle.smnavi.map.util.ConvertDtoUtil.convertRouteInfoToDto;

@Component
@RequiredArgsConstructor
@Transactional
public class TransitRouteApi {

    private final TransitRepository transitRepository;
    private final RouteDetailPositionApi routeDetailPositionApi;
    private final ApiConstantValue apiConstantValue;
    private final String HOST_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";



    public List<TransitPathDto> getTransitRoute(String startX, String startY, String numbers) {
        JSONObject transitJson = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", apiConstantValue.getOdsayApiKey()),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", apiConstantValue.getSMU_X()),
                new ApiKeyValue("EY", apiConstantValue.getSMU_Y()));

        StringTokenizer st = new StringTokenizer(numbers, ",");
        List<Integer> numList = new ArrayList<>();
        while (st.hasMoreTokens()){
            numList.add(Integer.parseInt(st.nextToken()));
        }

        List<TransitPathDto> transitPathDtoList = makePathDtoList(transitJson, numList);

        makeEdgeAndRoute(transitPathDtoList);

        return transitPathDtoList;
    }

    private void makeEdgeAndRoute(List<TransitPathDto> transitPathDtoList) {

        for (int i = 0; i < transitPathDtoList.size(); i++) {
            TransitPathDto transitPathDto = transitPathDtoList.get(i);
            List<TransitSubPathDto> transitSubPathDtoList = transitPathDto.getSubPathList();
            List<List<Edge>> edgeLists = new ArrayList<>();

            int time = transitPathDto.getTime();

            for (TransitSubPathDto transitSubPathDto : transitSubPathDtoList) {

                Station preStation = null;

                List<StationDto> stationDtoList = transitSubPathDto.getStationList();
                List<Station> stationList = stationDtoList.stream()
                        .map(dto -> dto.toEntity(transitSubPathDto.getType()))
                        .collect(Collectors.toList());
                stationList = transitRepository.saveStations(stationList);

                List<Edge> edgeList = new ArrayList<>();
                for (Station station : stationList) {
                    if (preStation != null) {
                        Edge edge = Edge.builder()
                                .src(preStation)
                                .dst(station)
                                .detailExist(false)
                                .build();
                        edgeList.add(edge);
                    }
                    preStation = station;
                }
                edgeLists.add(edgeList);
            }

            String[] mapObjArr = transitPathDto.getMapObj().split("@");
            boolean b = true;
            List<Edge> entireEdgeList = new ArrayList<>();
            for (int j = 0; j < edgeLists.size(); j++) {
                List<Edge> edgeList = edgeLists.get(j);
                List<Edge> persistedEdgeList = transitRepository.saveEdges(edgeList);
                entireEdgeList.addAll(persistedEdgeList);
                routeDetailPositionApi
                        .makeDetailPositionList(
                                transitPathDto.getSubPathList().get(j),
                                mapObjArr[j],
                                persistedEdgeList);

            }
            int size = entireEdgeList.size();
            boolean isMain = true;
            for (int j = 0; j < size; j++) {
                if (transitRepository.findRouteByEdgeList(entireEdgeList).isEmpty()) {
                Route route = transitRepository.saveRoute(entireEdgeList.get(0).getSrc(), time);
                transitRepository.saveRouteInfo(entireEdgeList, route, isMain);
                entireEdgeList.remove(0);
                time = time >= 3 ? time - 3 : 0;
                isMain = false;
                }
                else {
                    break;
                }
            }
        }

    }

    private List<TransitPathDto> makePathDtoList(JSONObject transitJson, List<Integer> numbers){
        List<TransitPathDto> transitPathDtoList = new ArrayList<>();

        JSONArray pathList = transitJson.getJSONObject("result").getJSONArray("path");

        for (Integer i : numbers) {
            JSONObject path = pathList.getJSONObject(i);
            JSONObject pathInfo = path.getJSONObject("info");
            int time = pathInfo.getInt("totalTime");

            String mapObj = pathInfo.getString("mapObj");

            List<TransitSubPathDto> transitSubPathDtoList = makeSubPathDtoList(path);

            transitPathDtoList.add(TransitPathDto.builder()
                    .subPathList(transitSubPathDtoList)
                    .time(time)
                    .mapObj(mapObj)
                    .build());
        }
        return transitPathDtoList;
    }

    private List<TransitSubPathDto> makeSubPathDtoList(JSONObject path){
        List<TransitSubPathDto> transitSubPathDtoList = new ArrayList<>();
        StationDto preStationDto = null;

        JSONArray subPathList = path.getJSONArray("subPath");

        for (int i = 0; i < subPathList.length(); i++) {
            JSONObject subPath = subPathList.getJSONObject(i);

            String laneName = null;
            String from = null;
            String to = null;
            List<StationDto> stationDtoList = null;

            int trafficType = subPath.getInt("trafficType");
            TransitType type = TransitType.of(trafficType);
            int sectionTime = subPath.getInt("sectionTime");

            if (!(type == TransitType.WALK && i == 0)){
                from = subPath.getString("startName");
                to = subPath.getString("endName");

                JSONObject lane = subPath.getJSONArray("lane").getJSONObject(0);
                if(type == TransitType.BUS){
                    laneName = lane.getString("busNo");
                }
                else{
                    laneName = String.valueOf(lane.getInt("subwayCode"));
                }

                stationDtoList = makeStationDtoList(subPath, laneName);

            }

            transitSubPathDtoList.add(TransitSubPathDto.builder()
                    .type(type)
                    .laneName(laneName)
                    .from(from)
                    .to(to)
                    .sectionTime(sectionTime)
                    .stationList(stationDtoList)
                    .build());
        }
        return transitSubPathDtoList;
    }


    private List<StationDto> makeStationDtoList(JSONObject subPath, String laneName){
        List<StationDto> stationDtoList = new ArrayList<>();

        JSONArray stationList = subPath.getJSONObject("passStopList").getJSONArray("stations");

        for (int i = 0; i < stationList.length(); i++) {
            JSONObject station = stationList.getJSONObject(i);
            String localStationId = station.getString("localStationID");
            String stationName = station.getString("stationName");
            String x = station.getString("x");
            String y = station.getString("y");

            StationDto stationDto = StationDto.builder()
                    .localStationId(localStationId)
                    .busName(laneName)
                    .stationName(stationName)
                    .gpsX(x)
                    .gpsY(y)
                    .build();

            stationDtoList.add(stationDto);

        }
        return stationDtoList;
    }
}
