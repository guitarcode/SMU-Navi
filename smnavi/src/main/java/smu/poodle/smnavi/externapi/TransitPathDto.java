package smu.poodle.smnavi.externapi;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransitPathDto {
    private int transitInfoCnt;
    private List<TransitSubPathDto> subPathList;
    private int time;

    public TransitPathDto(List<TransitSubPathDto> transitInfoList, int time) {
        this.subPathList = transitInfoList;
        this.time = time;
        this.transitInfoCnt = transitInfoList.size();
    }
}
