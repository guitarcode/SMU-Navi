package smu.poodle.smnavi.map.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.dto.PathDto;

import java.util.List;

@Getter
@SuperBuilder
public class TransitResponse extends BaseResponse{

    private final int pathInfoCnt;
    private final List<PathDto.Info> pathInfoList;
}
