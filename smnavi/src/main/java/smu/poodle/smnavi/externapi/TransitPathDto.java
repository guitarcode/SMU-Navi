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
    private List<TransitSubPathDto> subPathList;
    private int time;

    @JsonIgnore
    private String mapObj;

    public TransitPathDto(List<TransitSubPathDto> transitInfoList, int time) {
        this.subPathList = transitInfoList;
        this.time = time;
    }
}
