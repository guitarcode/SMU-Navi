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
    String busName;
    String stationName;
    int busType;

    @Override
    public BusStation toEntity() {
        return BusStation.builder()
                .x(super.getGpsX())
                .y(super.getGpsY())
                .localStationId(this.localStationId)
                .busName(this.busName)
                .stationName(this.stationName)
                .busType(BusType.fromTypeNumber(busType))
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
