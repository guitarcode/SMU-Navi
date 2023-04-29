package smu.poodle.smnavi.externapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransitPathDto {
    //환승 경로를 각각의 Dto로 분리
    private List<TransitSubPathDto> subPathList;
    private int time;

    private int subPathCnt;

    @JsonIgnore
    private String mapObj;

    public TransitPathDto(List<TransitSubPathDto> transitInfoList, int time) {
        this.subPathList = transitInfoList;
        this.time = time;
    }
}
