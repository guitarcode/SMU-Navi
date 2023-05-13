package smu.poodle.smnavi.map.externapi.businfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeforeAfterStation {
    private String BeforeStationID;
    private String AfterStationID;
}
