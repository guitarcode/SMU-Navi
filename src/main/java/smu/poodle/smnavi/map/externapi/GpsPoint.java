package smu.poodle.smnavi.map.externapi;

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
