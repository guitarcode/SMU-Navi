package smu.poodle.smnavi.map.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.station.SubwayStation;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubwayStationDto extends WaypointDto {
    Integer stationId; //역 아이디
    String lineName; //호선 이름
    String stationName;
    @Override
    public SubwayStation toEntity() {
        return SubwayStation.builder()
                .x(super.getGpsX())
                .y(super.getGpsY())
                .stationId(this.stationId)
                .lineName(this.lineName)
                .stationName(this.stationName)
                .build();
    }

//        public SubwayStationDto(String gpsX, String gpsY, Integer stationId, String lineName, String stationName) {
//            super(gpsX, gpsY);
//            this.stationId = stationId;
//            this.lineName = lineName;
//            this.stationName = stationName;
//        }
}