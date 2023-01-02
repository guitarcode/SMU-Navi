package smu.poodle.smnavi.externapi;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TransitPathInfoDto {
    private int transitInfoCnt;
    private List<TransitInfo> transitInfoList;
    private int gpsPointCnt;
    private List<GpsPoint> gpsPointList;
    private int time;

    public TransitPathInfoDto(List<TransitInfo> transitInfoList, List<GpsPoint> gpsPointList, int time) {
        this.transitInfoList = transitInfoList;
        this.gpsPointList = gpsPointList;
        this.time = time;
        this.transitInfoCnt = transitInfoList.size();
        this.gpsPointCnt = gpsPointList.size();
    }

    @Getter
    @Builder
    public static class TransitInfo {
        private TRANSIT type;
        private String name;
        private String from;
        private String to;
    }
}
