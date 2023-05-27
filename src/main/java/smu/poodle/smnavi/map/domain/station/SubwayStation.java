package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubwayStation extends Waypoint {
    Integer stationId; //역 아이디
    String lineName; //역 번호
    String stationName;

    @Builder
    public SubwayStation(String x, String y, Integer stationId, String lineName, String stationName) {
        super(x, y);
        this.stationId = stationId;
        this.lineName = lineName;
        this.stationName = stationName;
    }
}
