package smu.poodle.smnavi.map.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.user.auth.Kind;

public class AccidentDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {
        Long id;
        String kind;
        WaypointDto station;

        public static AccidentDto.Info of(Accident accident) {
            return Info.builder()
                    .id(accident.getId())
                    .kind(accident.getMessage())
                    .station(accident.getWaypoint().toDto())
                    .build();
        }
    }
}
