package smu.poodle.smnavi.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import smu.poodle.smnavi.map.externapi.TransitType;

import java.util.List;

@Getter
@Builder
@Setter
public class TransitSubPathDto {

    private TransitType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String laneName;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String to;


    private int sectionTime;
    //출발역 목록
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StationDto> stationList;

    //상세 좌표
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DetailPositionDto> gpsDetail;

}
