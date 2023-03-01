package smu.poodle.smnavi.externapi;

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
    private List<StationDto> stationList;
    private List<DetailPositionDto> gpsDetail;

}
