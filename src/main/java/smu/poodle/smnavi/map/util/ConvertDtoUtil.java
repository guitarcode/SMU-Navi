package smu.poodle.smnavi.map.util;

import smu.poodle.smnavi.map.domain.Edge;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.externapi.TransitType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertDtoUtil {
    public static PathDto.Info convertRouteInfoToDto(List<RouteInfo> routeInfoList, int time){
        boolean isFirst = true;
        List<PathDto.TransitSubPathDto> subPathDto = new ArrayList<>();
        List<PathDto.WaypointDto> stationList = new ArrayList<>();
        List<PathDto.DetailPositionDto> gpsDetail = new ArrayList<>();
        PathDto.WaypointDto waypointDto = null;
        String lastLaneName = "";
        PathDto.TransitSubPathDto transitSubPathDto = PathDto.TransitSubPathDto.builder().build();
        Edge lastEdge = null;

        for (int i = 0; i < routeInfoList.size(); i++) {

            RouteInfo routeInfo = routeInfoList.get(i);
            Edge edge = routeInfo.getEdge();

            if(edge.getSrc() == null){
                transitSubPathDto = PathDto.TransitSubPathDto.builder()
                        .type(TransitType.WALK)
                        .sectionTime(edge.getWalkingTime())
                        .gpsDetail(edge.getDetailPositionList().stream()
                                .map(PathDto.DetailPositionDto::new)
                                .collect(Collectors.toList()))
                        .build();

                subPathDto.add(transitSubPathDto);

                transitSubPathDto = PathDto.TransitSubPathDto.builder().build();
                continue;
            }

            lastEdge = edge;
            Waypoint src = routeInfo.getEdge().getSrc();
            String curLaneName = src.getBusName();


            if(!lastLaneName.equals(curLaneName) & !isFirst){
                Waypoint dst = lastEdge.getDst();
                waypointDto = PathDto.WaypointDto.builder()
                        .stationId(dst.getStationId())
                        .stationName(dst.getStationName())
                        .busName(dst.getBusName())
                        .gpsX(dst.getX())
                        .gpsY(dst.getY())
                        .build();

                stationList.add(waypointDto);
                transitSubPathDto.setStationList(stationList);
                transitSubPathDto.setGpsDetail(gpsDetail);
                transitSubPathDto.setTo(waypointDto.getStationName());
                subPathDto.add(transitSubPathDto);

                stationList = new ArrayList<>();
                gpsDetail = new ArrayList<>();

                isFirst = true;
            }
            if(isFirst) {
                lastLaneName = src.getBusName();
                transitSubPathDto = PathDto.TransitSubPathDto.builder()
                        .type(src.getType())
                        .from(src.getStationName())
                        .laneName(lastLaneName)
                        .build();
                isFirst = false;
            }

            waypointDto = PathDto.WaypointDto.builder()
                    .stationId(src.getStationId())
                    .busName(src.getBusName())
                    .stationName(src.getStationName())
                    .gpsX(src.getX())
                    .gpsY(src.getY())
                    .build();

            stationList.add(waypointDto);
            gpsDetail.addAll(edge.getDetailPositionList().stream().map(PathDto.DetailPositionDto::new).collect(Collectors.toList()));
        }

//        RouteInfo routeInfo = routeInfoList.get(routeInfoList.size() - 1);
//        Station dst = routeInfo.getEdge().getDst();
//        stationDto = StationDto.builder()
//                .localStationId(dst.getLocalStationId())
//                .stationName(dst.getStationName())
//                .busName(dst.getBusName())
//                .gpsX(dst.getX())
//                .gpsY(dst.getY())
//                .build();
//
//        stationList.add(stationDto);
//        transitSubPathDto.setStationList(stationList);
//        transitSubPathDto.setGpsDetail(gpsDetail);
//        transitSubPathDto.setTo(stationDto.getStationName());
//        subPathDto.add(transitSubPathDto);

        return PathDto.Info.builder()
                .subPathList(subPathDto)
                .time(time)
                .subPathCnt(subPathDto.size())
                .build();
    }
}
