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
    String busName;
    String stationName;

    @Enumerated(EnumType.STRING)
    BusType busType;

    @Override
    public String getPointName() {
        return this.stationName;
    }

    @Override
    public WaypointDto toDto(){
        return BusStationDto.builder()
                .gpsX(super.getX())
                .gpsY(super.getY())
                .localStationId(localStationId)
                .busName(busName)
                .stationName(stationName)
                //todo: 버스타입을 무슨 값으로 돌릴지 상의 필요
                .busType(busType.getTypeNumber())
                .build();
    }
}
