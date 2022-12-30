package smu.poodle.smnavi.callapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransportInfo {
    private TRANSIT type;
    private String name;
    private String from;
    private String to;
}
