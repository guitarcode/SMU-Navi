package smu.poodle.smnavi.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.poodle.smnavi.map.domain.DetailPosition;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.domain.station.BusStation;
import smu.poodle.smnavi.map.domain.station.BusType;
import smu.poodle.smnavi.map.domain.station.SubwayStation;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.externapi.TransitType;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PathDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        List<TransitSubPathDto> subPathList;
        int time;

        int subPathCnt;

        @JsonIgnore
        String mapObj;

        public Info(List<TransitSubPathDto> transitInfoList, int time) {
            this.subPathList = transitInfoList;
            this.time = time;
        }
    }

    @Getter
    @Builder
    @Setter
    public static class TransitSubPathDto {
        TransitType type;
        String laneName;
        String from;
        String to;
        int sectionTime;
        List<WaypointDto> stationList;
        List<DetailPositionDto> gpsDetail;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class WaypointDto {
        private String gpsX;
        private String gpsY;

        public Waypoint toEntity() {
            return Waypoint.builder()
                    .x(this.gpsX)
                    .y(this.gpsY)
                    .build();
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class BusStationDto extends WaypointDto {
        final String localStationId;
        final String busName;
        final String stationName;
        final int busType;

        @Override
        public BusStation toEntity() {
            return BusStation.builder()
                    .x(super.getGpsX())
                    .y(super.getGpsY())
                    .localStationId(this.localStationId)
                    .busName(this.busName)
                    .stationName(this.stationName)
                    .busType(BusType.fromTypeNumber(busType))
                    .build();
        }

        @Builder
        public BusStationDto(String gpsX, String gpsY, String localStationId, String busName, String stationName, int busType) {
            super(gpsX, gpsY);
            this.localStationId = localStationId;
            this.busName = busName;
            this.stationName = stationName;
            this.busType = busType;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SubwayStationDto extends WaypointDto {
        final Integer stationId; //역 아이디
        final String lineName; //호선 이름
        final String stationName;

        @Override
        public SubwayStation toEntity() {
            return SubwayStation.builder()
                    .x(super.getGpsX())
                    .y(super.getGpsY())
                    .stationId(this.stationId)
                    .lineName(this.lineName)
                    .stationName(this.stationName)
                    .build();
        }

        @Builder
        public SubwayStationDto(String gpsX, String gpsY, Integer stationId, String lineName, String stationName) {
            super(gpsX, gpsY);
            this.stationId = stationId;
            this.lineName = lineName;
            this.stationName = stationName;
        }
    }

    @Data
    @AllArgsConstructor
    public static class DetailPositionDto {
        private String gpsX;
        private String gpsY;

        public DetailPositionDto(DetailPosition detailPosition) {
            this.gpsX = detailPosition.getX();
            this.gpsY = detailPosition.getY();
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
