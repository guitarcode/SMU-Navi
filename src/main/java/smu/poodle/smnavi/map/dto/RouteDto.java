package smu.poodle.smnavi.map.dto;

import lombok.Data;
import smu.poodle.smnavi.map.domain.Route;

public class RouteDto {
    @Data
    public static class Info {
        private String startStationId;
        private String startStationName;
        private String x;
        private String y;

        public Info(Route route) {
//            this.startStationId = route.getStartStation().getStationId();
//            this.startStationName = route.getStartStation().getStationName();
            this.x = route.getStartStation().getX();
            this.y = route.getStartStation().getY();
        }
    }
}
