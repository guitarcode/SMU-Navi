package smu.poodle.smnavi.map.externapi.busarrinfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccidentData {
    public int stationId;

    public String message;
}
