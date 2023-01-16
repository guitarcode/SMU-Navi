package smu.poodle.smnavi.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.externapi.TransitPathDto;

import java.util.List;

@Getter
@SuperBuilder
public class TransitResponse extends BaseResponse{

    private final int pathInfoCnt;
    private final List<TransitPathDto> pathInfoList;
}
