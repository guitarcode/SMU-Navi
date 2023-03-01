package smu.poodle.smnavi.externapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class GpsPoint {
    private final String gpsX;
    private final String gpsY;

}
