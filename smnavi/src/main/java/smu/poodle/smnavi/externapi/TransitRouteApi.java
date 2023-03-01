package smu.poodle.smnavi.externapi;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.Edge;
import smu.poodle.smnavi.domain.Route;
import smu.poodle.smnavi.domain.Station;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class TransitRouteApi {

    private final TransitRepository transitRepository;
    private final RouteDetailPositionApi routeDetailPositionApi;
    private final ApiConstantValue apiConstantValue;
    private final String HOST_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";


    public List<TransitPathDto> getTransitRoute(String startX, String startY) {
        JSONObject transitJson = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", apiConstantValue.getOdsayApiKey()),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", apiConstantValue.getSMU_X()),
                new ApiKeyValue("EY", apiConstantValue.getSMU_Y()));

        List<TransitPathDto> transitPathDtoList = makePathDtoList(transitJson);

        makeEdgeAndRoute(transitPathDtoList);

        return transitPathDtoList;
    }

    private void makeEdgeAndRoute(List<TransitPathDto> transitPathDtoList) {

//        for (TransitPathDto transitPathDto : transitPathDtoList) {
        TransitPathDto transitPathDto = transitPathDtoList.get(0);
            List<TransitSubPathDto> transitSubPathDtoList = transitPathDto.getSubPathList();
            List<List<Edge>> edgeLists = new ArrayList<>();

            int time = transitPathDto.getTime();

            for (TransitSubPathDto transitSubPathDto : transitSubPathDtoList) {
                
                Station preStation = null;
                
                List<StationDto> stationDtoList = transitSubPathDto.getStationList();
                List<Station> stationList = stationDtoList.stream()
                        .map(StationDto::toEntity)
                        .collect(Collectors.toList());
                transitRepository.saveStations(stationList);

                List<Edge> edgeList = new ArrayList<>();
                for (Station station : stationList) {
                    if(preStation != null) {
                        Edge edge = Edge.builder()
                                .src(preStation)
                                .dst(station)
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
            for (int i = 0; i < edgeLists.size(); i++) {
                List<Edge> edgeList = edgeLists.get(i);
                List<Edge> persistedEdgeList = transitRepository.saveEdges(edgeList);
                entireEdgeList.addAll(persistedEdgeList);
                routeDetailPositionApi
                        .makeDetailPositionList(
                            transitPathDto.getSubPathList().get(i),
                            mapObjArr[i],
                            persistedEdgeList);

            }
            if(b) {
                Route route = transitRepository.saveRoute(entireEdgeList.get(0).getSrc(), time);
                transitRepository.saveRouteInfo(entireEdgeList, route);
            }


    }

    private List<TransitPathDto> makePathDtoList(JSONObject transitJson){
        List<TransitPathDto> transitPathDtoList = new ArrayList<>();

        JSONArray pathList = transitJson.getJSONObject("result").getJSONArray("path");
        for (int i = 0; i < pathList.length(); i++) {
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

            int trafficType = subPath.getInt("trafficType");
            TransitType type = TransitType.of(trafficType);
            int sectionTime = subPath.getInt("sectionTime");

            if(!type.equals(TransitType.WALK)){
                String from = subPath.getString("startName");
                String to = subPath.getString("endName");

                JSONObject lane = subPath.getJSONArray("lane").getJSONObject(0);
                String laneName;
                if(type == TransitType.BUS){
                    laneName = lane.getString("busNo");
                }
                else{
                    laneName = String.valueOf(lane.getInt("subwayCode"));
                }

                List<StationDto> stationDtoList = makeStationDtoList(subPath, laneName);

                transitSubPathDtoList.add(TransitSubPathDto.builder()
                        .type(type)
                        .laneName(laneName)
                        .from(from)
                        .to(to)
                        .sectionTime(sectionTime)
                        .stationList(stationDtoList)
                        .build());
            }
        }
        return transitSubPathDtoList;
    }


    private List<StationDto> makeStationDtoList(JSONObject subPath, String laneName){
        List<StationDto> stationDtoList = new ArrayList<>();

        JSONArray stationList = subPath.getJSONObject("passStopList").getJSONArray("stations");

        for (int i = 0; i < stationList.length(); i++) {
            JSONObject station = stationList.getJSONObject(i);
            String stationName = station.getString("stationName");
            int stationId = station.getInt("stationID");
            String x = station.getString("x");
            String y = station.getString("y");

            StationDto stationDto = StationDto.builder()
                    .stationId(laneName+":"+stationId)
                    .name(stationName)
                    .gpsX(x)
                    .gpsY(y)
                    .build();

            stationDtoList.add(stationDto);

        }
        return stationDtoList;
    }
}
