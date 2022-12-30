package smu.poodle.smnavi.callapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PathInfo {
    private List<TransportInfo> transportInfoList;
    private List<GpsPoint> gpsPointList;
    private int time;

    @Override
    public String toString() {
        return "PathInfo{" +
                "busInfoList=" + transportInfoList +
                ", gpsPointList=" + gpsPointList +
                ", time=" + time +
                '}';
    }
}
