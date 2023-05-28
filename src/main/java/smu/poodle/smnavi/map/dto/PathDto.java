package smu.poodle.smnavi.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.poodle.smnavi.map.domain.data.BusType;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
import smu.poodle.smnavi.map.domain.mapping.SubPathAndEdge;
import smu.poodle.smnavi.map.domain.path.DetailPosition;
import smu.poodle.smnavi.map.domain.path.Edge;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.domain.data.TransitType;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PathDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        int time;
        int subPathCnt;
        List<SubPathDto> subPathList;
        @JsonIgnore
        String mapObj;

        public static Info fromEntity(FullPath fullPath){
            List<SubPath> subPaths = fullPath.getSubPaths().stream()
                    .map(FullPathAndSubPath::getSubPath).toList();

            List<PathDto.SubPathDto> subPathDtos = new ArrayList<>();

            for (SubPath subPath : subPaths) {
                //엣지를 경유 WaypointDto 로 변환하는 작업
                //dto 에 팩토리메서드패턴을 구현하여 edge 의 정보를 waypoint 와 detailPosition 으로 변환하고
                //변환된 정보를 subPathDto 로 담아 생성함
                List<Edge> edges = subPath.getEdgeInfos().stream().map(SubPathAndEdge::getEdge).toList();

                subPathDtos.add(PathDto.SubPathDto.makeSubPathDtoWithEdges(subPath, edges));
            }

            return Info.builder()
                    .subPathList(subPathDtos)
                    .time(fullPath.getTotalTime())
                    .subPathCnt(subPaths.size())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    public static class SubPathDto {
        TransitType transitType;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String busType;
        @JsonIgnore
        Integer busTypeInt;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String lineName;
        String from;
        String to;
        Integer sectionTime;


        List<WaypointDto> stationList;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<DetailPositionDto> gpsDetail;

        public static SubPathDto makeSubPathDtoWithEdges(SubPath subPath, List<Edge> edges){
            return SubPathDto.builder()
                    .transitType(subPath.getTransitType())
                    .sectionTime(subPath.getSectionTime())
                    .stationList(WaypointDto.edgesToWaypointDtos(edges))
                    .from(subPath.getToName())
                    .to(subPath.getToName())
                    .gpsDetail(DetailPositionDto.edgesToDetailPositionDtos(edges))
                    .from(subPath.getFromName())
                    .to(subPath.getToName())
                    .build();
        }

    }



//    @EqualsAndHashCode(callSuper = true)
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    @Data
//    @SuperBuilder
//    public static class SubwayStationDto extends WaypointDto {
//        final Integer stationId; //역 아이디
//        final String lineName; //호선 이름
//        final String stationName;
//
//        @Override
//        public SubwayStation toEntity() {
//            return SubwayStation.builder()
//                    .x(super.getGpsX())
//                    .y(super.getGpsY())
//                    .stationId(this.stationId)
//                    .lineName(this.lineName)
//                    .stationName(this.stationName)
//                    .build();
//        }
//
////        public SubwayStationDto(String gpsX, String gpsY, Integer stationId, String lineName, String stationName) {
////            super(gpsX, gpsY);
////            this.stationId = stationId;
////            this.lineName = lineName;
////            this.stationName = stationName;
////        }
//    }

    @Data
    @AllArgsConstructor
    public static class DetailPositionDto {
        private String gpsX;
        private String gpsY;

        public DetailPositionDto(DetailPosition detailPosition) {
            this.gpsX = detailPosition.getX();
            this.gpsY = detailPosition.getY();
        }

        public static List<DetailPositionDto> edgesToDetailPositionDtos(List<Edge> edges) {
            List<DetailPositionDto> detailPositionDtos = new ArrayList<>();

            for (Edge edge : edges) {
                detailPositionDtos.addAll(edge.getDetailPositionList()
                        .stream().map(PathDto.DetailPositionDto::new).toList());
            }

            return detailPositionDtos;
        }
    }



//    @Data
//    public static class RouteInfoDto {
//        private int idx;
//        private String srcId;
//        private String src;
//        private String dstId;
//        private String dst;
//
//        public RouteInfoDto(int idx, RouteInfo routeInfo) {
//            this.idx = idx;
//            this.srcId = routeInfo.getEdge().getSrc().getStationId();
//            this.src = routeInfo.getEdge().getSrc().getStationName();
//            this.dstId = routeInfo.getEdge().getDst().getStationId();
//            this.dst = routeInfo.getEdge().getDst().getStationName();
//        }
//    }
}
