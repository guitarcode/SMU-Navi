package smu.poodle.smnavi.map.odsay;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.map.domain.DetailPosition;
import smu.poodle.smnavi.map.domain.Edge;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.externapi.ApiConstantValue;
import smu.poodle.smnavi.map.externapi.ApiKeyValue;
import smu.poodle.smnavi.map.externapi.ApiUtilMethod;
import smu.poodle.smnavi.map.externapi.TransitType;
import smu.poodle.smnavi.map.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class TransitRouteApi {


    private final TransitRepository transitRepository;
    private final RouteDetailPositionApi routeDetailPositionApi;
    private final ApiConstantValue apiConstantValue;


    public List<PathDto.Info> getTransitRoute(String startX, String startY, String numbers) {

        String HOST_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";

        JSONObject transitJson = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", apiConstantValue.getOdsayApiKey()),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", apiConstantValue.getSMU_X()),
                new ApiKeyValue("EY", apiConstantValue.getSMU_Y()));

        StringTokenizer st = new StringTokenizer(numbers, ",");
        List<Integer> numList = new ArrayList<>();
        while (st.hasMoreTokens()) {
            numList.add(Integer.parseInt(st.nextToken()));
        }

        List<PathDto.Info> transitInfoList = makePathDtoList(transitJson, numList);

        makeEdgeAndRoute(transitInfoList);

        return transitInfoList;
    }

    private void makeEdgeAndRoute(List<PathDto.Info> transitInfoList) {

        for (PathDto.Info transitInfo : transitInfoList) {
            List<PathDto.TransitSubPathDto> transitSubPathDtoList = transitInfo.getSubPathList();
            List<List<Edge>> edgeLists = new ArrayList<>();

            int time = transitInfo.getTime();

            for (PathDto.TransitSubPathDto transitSubPathDto : transitSubPathDtoList) {

                List<Edge> edgeList = new ArrayList<>();

                if (transitSubPathDto.getType() == TransitType.WALK) {
                    Edge edge = Edge.builder()
                            .src(null)
                            .dst(null)
                            .detailExist(false)
                            .walkingTime(transitSubPathDto.getSectionTime())
                            .build();

                    edgeList.add(edge);
                    edgeLists.add(edgeList);
                    continue;
                }
                Waypoint preStation = null;

                List<PathDto.WaypointDto> waypointDtoList = transitSubPathDto.getStationList();
                List<Waypoint> stationList = waypointDtoList.stream()
                        .map(PathDto.WaypointDto::toEntity)
                        .collect(Collectors.toList());
                stationList = transitRepository.saveStations(stationList);

                for (Waypoint station : stationList) {
                    if (preStation != null) {
                        Edge edge = Edge.builder()
                                .src(preStation)
                                .dst(station)
                                .detailExist(false)
                                .walkingTime(transitSubPathDto.getSectionTime())
                                .build();
                        edgeList.add(edge);
                    }
                    preStation = station;
                }
                edgeLists.add(edgeList);
            }

            String[] mapObjArr = transitInfo.getMapObj().split("@");
            List<Edge> entireEdgeList = new ArrayList<>();

            int k = 0;
            for (int j = 0; j < edgeLists.size(); j++) {
                List<Edge> edgeList = edgeLists.get(j);

                if (edgeList.get(0).getDst() == null) {
                    Edge walkEdge = edgeList.get(0);
                    List<PathDto.DetailPositionDto> detailPositionDtos = new ArrayList<>();
                    if (j != 0) {
                        int edgeSize = edgeLists.get(j - 1).size();
                        detailPositionDtos.add(new PathDto.DetailPositionDto(
                                edgeLists.get(j - 1).get(edgeSize - 1).getDst().getX(),
                                edgeLists.get(j - 1).get(edgeSize - 1).getDst().getY()
                        ));
                    }
                    if (j != edgeLists.size() - 1) {
                        detailPositionDtos.add(new PathDto.DetailPositionDto(
                                edgeLists.get(j + 1).get(0).getSrc().getX(),
                                edgeLists.get(j + 1).get(0).getSrc().getY()
                        ));
                    } else {
                        detailPositionDtos.add(new PathDto.DetailPositionDto(
                                "126.955252",
                                "37.602638"
                        ));
                    }
                    walkEdge.setDetailPositionList(detailPositionDtos.stream().map(
                            detailPositionDto -> {
                                return DetailPosition.builder()
                                        .edge(walkEdge)
                                        .x(detailPositionDto.getGpsX())
                                        .y(detailPositionDto.getGpsY())
                                        .build();
                            }
                    ).collect(Collectors.toList()));

                    List<Edge> persistedEdgeList = transitRepository.saveWalkingEdge(walkEdge);
                    entireEdgeList.addAll(persistedEdgeList);
                    transitInfo.getSubPathList().get(j).setGpsDetail(detailPositionDtos);
                    continue;
                }
                List<Edge> persistedEdgeList = transitRepository.saveEdges(edgeList);
                entireEdgeList.addAll(persistedEdgeList);
                routeDetailPositionApi
                        .makeDetailPositionList(
                                transitInfo.getSubPathList().get(j),
                                mapObjArr[k],
                                persistedEdgeList);
                k++;

            }
            int size = entireEdgeList.size();
            boolean isMain = true;
            for (int j = 0; j < size; j++) {
                if (transitRepository.findRouteByEdgeList(entireEdgeList).isEmpty()) {
                    Route route = transitRepository.saveRoute(entireEdgeList.get(0).getSrc(), time);
                    transitRepository.saveRouteInfo(entireEdgeList, route, isMain);
                    entireEdgeList.remove(0);
                    isMain = false;
                } else {
                    break;
                }
            }
        }

    }

    private List<PathDto.Info> makePathDtoList(JSONObject transitJson, List<Integer> numbers) {
        List<PathDto.Info> transitInfoList = new ArrayList<>();

        JSONArray pathList = transitJson.getJSONObject("result").getJSONArray("path");

        for (Integer i : numbers) {
            JSONObject path = pathList.getJSONObject(i);
            JSONObject pathInfo = path.getJSONObject("info");
            int time = pathInfo.getInt("totalTime");

            String mapObj = pathInfo.getString("mapObj");

            List<PathDto.TransitSubPathDto> transitSubPathDtoList = makeSubPathDtoList(path);

            transitInfoList.add(PathDto.Info.builder()
                    .subPathList(transitSubPathDtoList)
                    .time(time)
                    .mapObj(mapObj)
                    .build());
        }
        return transitInfoList;
    }

    private List<PathDto.TransitSubPathDto> makeSubPathDtoList(JSONObject path) {
        List<PathDto.TransitSubPathDto> transitSubPathDtoList = new ArrayList<>();
//        TransitPathDto.WaypointDto preWaypointDto = null;

        JSONArray subPathList = path.getJSONArray("subPath");

        for (int i = 0; i < subPathList.length(); i++) {
            JSONObject subPath = subPathList.getJSONObject(i);

            String laneName = null;
            String from = null;
            String to = null;
            List<PathDto.WaypointDto> waypointDtoList = null;

            int trafficType = subPath.getInt("trafficType");
            TransitType type = TransitType.of(trafficType);
            int sectionTime = subPath.getInt("sectionTime");

            if (type == TransitType.WALK) {
                if (i == 0 || sectionTime == 0) {
                    continue;
                }
            } else {
                from = subPath.getString("startName");
                to = subPath.getString("endName");

                JSONObject lane = subPath.getJSONArray("lane").getJSONObject(0);
                if (type == TransitType.BUS) {
                    laneName = lane.getString("busNo");
                } else {
                    laneName = String.valueOf(lane.getInt("subwayCode"));
                }

                waypointDtoList = makeStationDtoList(subPath, laneName, type);

            }

            transitSubPathDtoList.add(PathDto.TransitSubPathDto.builder()
                    .type(type)
                    .laneName(laneName)
                    .from(from)
                    .to(to)
                    .sectionTime(sectionTime)
                    .stationList(waypointDtoList)
                    .build());
        }
        return transitSubPathDtoList;
    }


    private List<PathDto.WaypointDto> makeStationDtoList(JSONObject subPath, String laneName, TransitType type) {
        List<PathDto.WaypointDto> waypointDtoList = new ArrayList<>();

        JSONArray stationList = subPath.getJSONObject("passStopList").getJSONArray("stations");

        for (int i = 0; i < stationList.length(); i++) {
            JSONObject station = stationList.getJSONObject(i);

            String stationName;

            String x = station.getString("x");
            String y = station.getString("y");
            stationName = station.getString("stationName");


            if (type == TransitType.BUS) {
                String stationId = station.getString("localStationID");
                int busTypeInt = station.getInt("type");

                waypointDtoList.add(PathDto.BusStationDto.builder()
                        .localStationId(stationId)
                        .busName(laneName)
                        .stationName(stationName)
                        .busType(busTypeInt)
                        .gpsX(x)
                        .gpsY(y)
                        .build());

            } else if (type == TransitType.SUBWAY) {
                int stationId = station.getInt("stationID");

                waypointDtoList.add(PathDto.SubwayStationDto.builder()
                                .stationId(stationId)
                                .lineName(laneName)
                                .stationName(stationName)
                                .gpsX(x)
                                .gpsY(y)
                        .build());
            }


        }
        return waypointDtoList;
    }
}
