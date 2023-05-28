package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.dto.SubwayStationDto;
import smu.poodle.smnavi.map.dto.WaypointDto;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubwayStation extends Waypoint {
    Integer stationId; //역 아이디
    String lineName; //역 번호
    String stationName;

    @Override
    public String getPointName() {
        return this.stationName;
    }

    @Override
    public String getStartStationId() {
        return this.stationId.toString();
    }


    @Override
    public WaypointDto toDto(){
        return SubwayStationDto.builder()
                .gpsX(super.getX())
                .gpsY(super.getY())
                .stationId(stationId)
                .lineName(lineName)
                //todo: 버스타입을 무슨 값으로 돌릴지 상의 필요
                .stationName(stationName)
                .build();
    }
}
