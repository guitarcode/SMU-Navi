package smu.poodle.smnavi.map.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class StationInfo {
    @Id
    private int stationId;

    private String lineName;
    private String stationName;
    private int stationNum;
    @Column(name = "gps_y")
    private String gpsY;
    @Column(name = "gps_X")
    private String gpsX;
}
