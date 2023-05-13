package smu.poodle.smnavi.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import smu.poodle.smnavi.map.domain.Station;
import smu.poodle.smnavi.map.externapi.TransitType;

@Getter
@Builder
@AllArgsConstructor
public class StationDto {
    private String localStationId;
    private String busName;
    private String stationName;
    private String gpsX;
    private String gpsY;

    public Station toEntity(TransitType type) {
        return Station.builder()
                .localStationId(this.localStationId)
                .busName(this.busName)
                .stationName(this.stationName)
                .x(this.gpsX)
                .y(this.gpsY)
                .type(type)
                .build();
    }
}