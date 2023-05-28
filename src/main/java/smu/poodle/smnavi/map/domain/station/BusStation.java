package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.data.BusType;
import smu.poodle.smnavi.map.dto.BusStationDto;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.dto.WaypointDto;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusStation extends Waypoint {
    String localStationId; //버스는 LocalStationId, 지하철은 StationId
    String stationName;

    @Override
    public String getPointName() {
        return this.stationName;
    }
    @Override
    public String getStartStationId() {
        return localStationId;
    }

    @Override
    public WaypointDto toDto(){
        return BusStationDto.builder()
                .gpsX(super.getX())
                .gpsY(super.getY())
                .localStationId(localStationId)
                .stationName(stationName)
                .build();
    }
}
