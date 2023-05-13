package smu.poodle.smnavi.map.dto;

import lombok.Data;
import smu.poodle.smnavi.map.domain.RouteInfo;

@Data
public class RouteInfoDto {
    private int idx;
    private String srcId;
    private String src;
    private String dstId;
    private String dst;

    public RouteInfoDto(int idx, RouteInfo routeInfo) {
        this.idx = idx;
        this.srcId = routeInfo.getEdge().getSrc().getLocalStationId();
        this.src = routeInfo.getEdge().getSrc().getStationName();
        this.dstId = routeInfo.getEdge().getDst().getLocalStationId();
        this.dst = routeInfo.getEdge().getDst().getStationName();
    }
}
