package smu.poodle.smnavi.map.dto;

import lombok.Data;
import smu.poodle.smnavi.map.domain.station.Waypoint;

public class RouteDto {
    @Data
    public static class Info {
        private String startStationId;
        private String startStationName;
        private String x;
        private String y;

        public Info(Waypoint waypoint) {
            this.startStationId = waypoint.getStartStationId();
            this.startStationName = waypoint.getPointName();
            this.x = waypoint.getX();
            this.y = waypoint.getY();
        }
    }
}
