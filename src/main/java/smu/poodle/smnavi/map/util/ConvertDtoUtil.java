//package smu.poodle.smnavi.map.util;
//
//import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
//import smu.poodle.smnavi.map.domain.mapping.SubPathAndEdge;
//import smu.poodle.smnavi.map.domain.path.*;
//import smu.poodle.smnavi.map.dto.PathDto;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ConvertDtoUtil {
//    public static PathDto.Info convertRouteInfoToDto(FullPath fullPath) {
//        List<PathDto.SubPathDto> subPathDtos = new ArrayList<>();
//
//        List<SubPath> subPaths = fullPath.getSubPaths().stream()
//                .map(FullPathAndSubPath::getSubPath).toList();
//
//        for (SubPath subPath : subPaths) {
//            //엣지를 경유 WaypointDto 로 변환하는 작업
//            //dto 에 팩토리메서드패턴을 구현하여 edge 의 정보를 waypoint 와 detailPosition 으로 변환하고
//            //변환된 정보를 subPathDto 로 담아 생성함
//            List<Edge> edges = subPath.getEdgeInfos().stream().map(SubPathAndEdge::getEdge).toList();
//
//            PathDto.SubPathDto.makeSubPathDtoWithEdges(subPath, edges);
//        }
//
//        PathDto.Info.builder()
//
//        boolean isFirst = true; // 처음으로 시작했다면 true
//
//
//        List<PathDto.SubPathDto> subPathDto = new ArrayList<>();
//        List<PathDto.WaypointDto> stationList = new ArrayList<>();
//        List<PathDto.DetailPositionDto> gpsDetail = new ArrayList<>();
//
//
//        PathDto.WaypointDto waypointDto;
//        String lastLaneName = "";
//        Edge lastEdge;
//
//        PathDto.SubPathDto transitSubPathDto = PathDto.SubPathDto.builder().build();
////
////        for (int i = 0; i < routeInfoList.size(); i++) {
////
////            RouteInfo routeInfo = routeInfoList.get(i);
////            Edge edge = routeInfo.getEdge();
////
////            if (edge.getTransitType() == TransitType.WALK) {
////                transitSubPathDto = PathDto.SubPathDto.builder()
////                        .transitType(TransitType.WALK)
////                        .sectionTime(edge.getTime())
////                        .gpsDetail(edge.getDetailPositionList().stream()
////                                .map(PathDto.DetailPositionDto::new)
////                                .collect(Collectors.toList()))
////                        .build();
////
////                subPathDto.add(transitSubPathDto);
////
////                transitSubPathDto = PathDto.SubPathDto.builder().build();
////                continue;
////            }
////
////            lastEdge = edge;
////            Waypoint src = routeInfo.getEdge().getSrc();
////
////            String curLaneName = src.getBusName();
////
////
////            if (!lastLaneName.equals(curLaneName) & !isFirst) {
////                Waypoint dst = lastEdge.getDst();
////                waypointDto = PathDto.WaypointDto.builder()
////                        .stationId(dst.getStationId())
////                        .stationName(dst.getStationName())
////                        .busName(dst.getBusName())
////                        .gpsX(dst.getX())
////                        .gpsY(dst.getY())
////                        .build();
////
////                stationList.add(waypointDto);
////                transitSubPathDto.setStationList(stationList);
////                transitSubPathDto.setGpsDetail(gpsDetail);
////                transitSubPathDto.setTo(waypointDto.getStationName());
////                subPathDto.add(transitSubPathDto);
////
////                stationList = new ArrayList<>();
////                gpsDetail = new ArrayList<>();
////
////                isFirst = true;
////            }
////            if (isFirst) {
////                lastLaneName = src.getBusName();
////                transitSubPathDto = PathDto.SubPathDto.builder()
////                        .transitType(src.getType())
////                        .from(src.getStationName())
////                        .laneName(lastLaneName)
////                        .build();
////                isFirst = false;
////            }
////
////            waypointDto = PathDto.WaypointDto.builder()
////                    .stationId(src.getStationId())
////                    .busName(src.getBusName())
////                    .stationName(src.getStationName())
////                    .gpsX(src.getX())
////                    .gpsY(src.getY())
////                    .build();
////
////            stationList.add(waypointDto);
////            gpsDetail.addAll(edge.getDetailPositionList().stream().map(PathDto.DetailPositionDto::new).collect(Collectors.toList()));
////        }
////
//////        RouteInfo routeInfo = routeInfoList.get(routeInfoList.size() - 1);
//////        Station dst = routeInfo.getEdge().getDst();
//////        stationDto = StationDto.builder()
//////                .localStationId(dst.getLocalStationId())
//////                .stationName(dst.getStationName())
//////                .busName(dst.getBusName())
//////                .gpsX(dst.getX())
//////                .gpsY(dst.getY())
//////                .build();
//////
//////        stationList.add(stationDto);
//////        transitSubPathDto.setStationList(stationList);
//////        transitSubPathDto.setGpsDetail(gpsDetail);
//////        transitSubPathDto.setTo(stationDto.getStationName());
//////        subPathDto.add(transitSubPathDto);
////
////        return PathDto.Info.builder()
////                .subPathList(subPathDto)
////                .time(time)
////                .subPathCnt(subPathDto.size())
////                .build();
//    }
//}
