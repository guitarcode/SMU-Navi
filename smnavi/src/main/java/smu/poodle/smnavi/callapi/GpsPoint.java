package smu.poodle.smnavi.callapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GpsPoint {
    private String gpsX;
    private String gpsY;

    @Override
    public String toString() {
        return "GpsPoint{" +
                "gpsX='" + gpsX + '\'' +
                ", gpsY='" + gpsY + '\'' +
                '}';
    }
}
