package smu.poodle.smnavi.externapi;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.Edge;
import smu.poodle.smnavi.domain.Route;
import smu.poodle.smnavi.domain.RouteInfo;
import smu.poodle.smnavi.domain.Station;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static smu.poodle.smnavi.externapi.ApiUtilMethod.calculateDistance;

@Component
@RequiredArgsConstructor
@Transactional
public class TransitRouteApi {

    private final TransitRepository transitRepository;
    private final RouteDetailPositionApi routeDetailPositionApi;
    private final ApiConstantValue apiConstantValue;
    private final String HOST_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";

    public List<TransitPathDto> getTransitRoute(String startX, String startY) {
        List<TransitPathDto> pathInfoList = new ArrayList<>();

        List<Route> routeList = transitRepository.findAllRouteSeenTrue();
        GpsPoint gpsPoint = new GpsPoint(startX, startY);

        for (Route route : routeList) {
            GpsPoint startPoint = new GpsPoint(route.getStartStation().getX(), route.getStartStation().getY());
            double distance = calculateDistance(gpsPoint, startPoint);
            if(distance > 300){
                continue;
            }
            else{
                List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
                TransitPathDto transitPathDto = convertRouteInfoToDto(routeInfoList, route.getTime());
                pathInfoList.add(transitPathDto);
            }
        }

        return pathInfoList;
    }

    //1번
    public List<TransitPathDto> getTransitRoute(String startStationId) {
        List<TransitPathDto> pathInfoList = new ArrayList<>();

        List<Route> routeList = transitRepository.findRouteByStartStationId(startStationId);

        for (Route route : routeList) {
                List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
                TransitPathDto transitPathDto = convertRouteInfoToDto(routeInfoList, route.getTime());
                pathInfoList.add(transitPathDto);
        }


        return pathInfoList;
    }


    public TransitPathDto convertRouteInfoToDto(List<RouteInfo> routeInfoList,int time){
        boolean isFirst = true;
        List<TransitSubPathDto> subPathDto = new ArrayList<>();
        List<StationDto> stationList = new ArrayList<>();
        List<DetailPositionDto> gpsDetail = new ArrayList<>();
        TransitSubPathDto transitSubPathDto = null;
        StationDto stationDto = null;
        String lastLaneName = "";

        for (int i = 0; i < routeInfoList.size(); i++) {
            RouteInfo routeInfo = routeInfoList.get(i);
            Edge edge = routeInfo.getEdge();
            Station src = routeInfo.getEdge().getSrc();
            String curLaneName = src.getBusName();

            if(!lastLaneName.equals(curLaneName) & !isFirst){
                transitSubPathDto.setStationList(stationList);
                transitSubPathDto.setGpsDetail(gpsDetail);
                transitSubPathDto.setTo(stationDto.getStationName());
                subPathDto.add(transitSubPathDto);

                stationList = new ArrayList<>();
                gpsDetail = new ArrayList<>();

                isFirst = true;
            }
            if(isFirst) {
                lastLaneName = src.getBusName();
                transitSubPathDto = TransitSubPathDto.builder()
                        .type(src.getType())
                        .from(src.getStationName())
                        .laneName(lastLaneName)
                        .build();
                isFirst = false;
            }

            stationDto = StationDto.builder()
                    .localStationId(src.getLocalStationId())
                    .busName(src.getBusName())
                    .stationName(src.getStationName())
                    .gpsX(src.getX())
                    .gpsY(src.getY())
                    .build();

            stationList.add(stationDto);
            gpsDetail.addAll(edge.getDetailPositionList().stream().map(DetailPositionDto::new).collect(Collectors.toList()));
        }

        RouteInfo routeInfo = routeInfoList.get(routeInfoList.size() - 1);
        Station dst = routeInfo.getEdge().getDst();
        stationDto = StationDto.builder()
                .localStationId(dst.getLocalStationId())
                .stationName(dst.getStationName())
                .busName(dst.getBusName())
                .gpsX(dst.getX())
                .gpsY(dst.getY())
                .build();

        stationList.add(stationDto);
        transitSubPathDto.setStationList(stationList);
        transitSubPathDto.setGpsDetail(gpsDetail);
        transitSubPathDto.setTo(stationDto.getStationName());
        subPathDto.add(transitSubPathDto);

        return TransitPathDto.builder()
                .subPathList(subPathDto)
                .time(time)
                .subPathCnt(subPathDto.size())
                .build();
    }

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
//        for (int i = 0; i < 5; i++) {
//            if(!numbers.contains(i)){
//                continue;
//            }
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
