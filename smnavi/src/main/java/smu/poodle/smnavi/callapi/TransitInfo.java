package smu.poodle.smnavi.callapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransitInfo {
    private TRANSIT type;
    private String name;
    private String from;
    private String to;
}
