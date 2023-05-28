package smu.poodle.smnavi.map.dto;

import lombok.Data;
import smu.poodle.smnavi.map.domain.path.FullPath;

public class RouteDto {
    @Data
    public static class Info {
        private String startStationId;
        private String startStationName;
        private String x;
        private String y;

        public Info(FullPath fullPath) {
            this.startStationId = fullPath.getStartWaypoint().getStartStationId();
            this.startStationName = fullPath.getStartWaypoint().getPointName();
            this.x = fullPath.getStartWaypoint().getX();
            this.y = fullPath.getStartWaypoint().getY();
        }
    }
}
