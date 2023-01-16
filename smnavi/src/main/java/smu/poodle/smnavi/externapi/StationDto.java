package smu.poodle.smnavi.externapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import smu.poodle.smnavi.domain.Station;

@Getter
@Builder
@AllArgsConstructor
public class StationDto {
    private int stationId;
    private String name;
    private String gpsX;
    private String gpsY;

    public Station toEntity() {
        return Station.builder()
                .id(this.stationId)
                .name(this.name)
                .startX(this.gpsX)
                .startY(this.gpsY)
                .build();
    }
}