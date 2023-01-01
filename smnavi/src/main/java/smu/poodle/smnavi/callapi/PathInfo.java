package smu.poodle.smnavi.callapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PathInfo {
    private int transitInfoCnt;
    private List<TransitInfo> transitInfoList;
    private int gpsPointCnt;
    private List<GpsPoint> gpsPointList;
    private int time;

    public PathInfo(List<TransitInfo> transitInfoList, List<GpsPoint> gpsPointList, int time) {
        this.transitInfoList = transitInfoList;
        this.gpsPointList = gpsPointList;
        this.time = time;
        this.transitInfoCnt = transitInfoList.size();
        this.gpsPointCnt = gpsPointList.size();
    }

}
