package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.dto.AbstractWaypointDto;
import smu.poodle.smnavi.map.dto.WaypointDto;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubwayStation extends Waypoint {
    Integer stationId; //역 아이디
    String stationName; //역 이름

    @Override
    public String getPointName() {
        return this.stationName;
    }

    @Override
    public String getStartStationId() {
        return this.stationId.toString();
    }


    @Override
    public AbstractWaypointDto toDto(){
        return WaypointDto.SubwayStationDto.builder()
                .gpsX(super.getX())
                .gpsY(super.getY())
                .stationId(stationId)
                //todo: 버스타입을 무슨 값으로 돌릴지 상의 필요
                .stationName(stationName)
                .build();
    }
}
