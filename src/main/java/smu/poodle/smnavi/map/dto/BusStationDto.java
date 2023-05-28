package smu.poodle.smnavi.map.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.data.BusType;
import smu.poodle.smnavi.map.domain.station.BusStation;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusStationDto extends WaypointDto {
    String localStationId;
    String stationName;

    @Override
    public BusStation toEntity() {
        return BusStation.builder()
                .x(super.getGpsX())
                .y(super.getGpsY())
                .localStationId(this.localStationId)
                .stationName(this.stationName)
                .build();
    }

//        public BusStationDto(String gpsX, String gpsY, String localStationId, String busName, String stationName, int busType) {
//            super(gpsX, gpsY);
//            this.localStationId = localStationId;
//            this.busName = busName;
//            this.stationName = stationName;
//            this.busType = busType;
//        }
}
