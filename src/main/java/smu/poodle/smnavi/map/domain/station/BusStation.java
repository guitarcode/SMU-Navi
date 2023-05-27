package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusStation extends Waypoint {
    String localStationId; //버스는 LocalStationId, 지하철은 StationId
    String busName;
    String stationName;

    @Enumerated(EnumType.STRING)
    BusType busType;

    @Builder
    public BusStation(String x, String y, String localStationId, String busName, String stationName, BusType busType) {
        super(x, y);
        this.localStationId = localStationId;
        this.busName = busName;
        this.stationName = stationName;
        this.busType = busType;
    }

}
