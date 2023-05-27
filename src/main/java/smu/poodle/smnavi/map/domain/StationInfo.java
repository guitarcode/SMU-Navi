package smu.poodle.smnavi.map.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationInfo {
    @Id
    Integer stationId;
    String lineName;
    String stationName;
    Integer stationNum;
    @Column(name = "gps_y")
    String gpsY;
    @Column(name = "gps_X")
    String gpsX;
}
