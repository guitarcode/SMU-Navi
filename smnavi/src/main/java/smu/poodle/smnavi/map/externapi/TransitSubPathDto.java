package smu.poodle.smnavi.map.externapi;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class TransitSubPathDto {
    private TransitType type;
    private String laneName;
    private String from;
    private String to;


    private int sectionTime;
    //출발역 목록
    private List<StationDto> stationList;

    //상세 좌표
    private List<DetailPositionDto> gpsDetail;

}
