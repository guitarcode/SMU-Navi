package smu.poodle.smnavi.externapi.businfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeforeAfterStation {
    private String BeforeStationID;
    private String AfterStationID;
}
