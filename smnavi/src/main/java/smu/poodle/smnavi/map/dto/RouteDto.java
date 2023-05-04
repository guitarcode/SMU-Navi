package smu.poodle.smnavi.map.dto;

import lombok.Data;
import smu.poodle.smnavi.map.domain.Route;

@Data
public class RouteDto {
    private String startStationId;
    private String startStationName;
    private String x;
    private String y;

    public RouteDto(Route route) {
        this.startStationId = route.getStartStation().getLocalStationId();
        this.startStationName = route.getStartStation().getStationName();
        this.x = route.getStartStation().getX();
        this.y = route.getStartStation().getY();
    }
}
