package smu.poodle.smnavi.externapi;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TransitPathInfoDto {
    private int transitInfoCnt;
    private List<TransitInfo> transitInfoList;
    private int gpsPointCnt;
    private int time;

    public TransitPathInfoDto(List<TransitInfo> transitInfoList, int time) {
        this.transitInfoList = transitInfoList;
        this.time = time;
        this.transitInfoCnt = transitInfoList.size();
    }

    @Getter
    @Builder
    public static class TransitInfo {
        private TRANSIT type;
        private String name;
        private String from;
        private String to;
        private List<String> stationList = new ArrayList<>();
        private List<GpsPoint> gpsPointList;

    }
}
