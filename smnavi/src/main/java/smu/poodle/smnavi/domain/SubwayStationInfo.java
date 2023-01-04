package smu.poodle.smnavi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class SubwayStationInfo {
    @Id
    private int stationId;

    private String lineName;
    private String stationName;
    private int stationNum;
    private String gpsY;
    private String gpsX;
}
