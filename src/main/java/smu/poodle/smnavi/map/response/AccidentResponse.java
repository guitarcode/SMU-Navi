package smu.poodle.smnavi.map.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.dto.AccidentDto;
import smu.poodle.smnavi.map.dto.PathDto;

import java.util.List;

@Getter
@SuperBuilder
public class AccidentResponse extends BaseResponse{

    private final List<AccidentDto.Info> data;
}
