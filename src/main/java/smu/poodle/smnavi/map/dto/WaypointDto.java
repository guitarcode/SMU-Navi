package smu.poodle.smnavi.map.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.domain.path.Edge;
import smu.poodle.smnavi.map.domain.station.Waypoint;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaypointDto {
    private String gpsX;
    private String gpsY;


    public Waypoint toEntity() {
        return Waypoint.builder()
                .x(this.gpsX)
                .y(this.gpsY)
                .build();
    }

    public static List<WaypointDto> edgesToWaypointDtos(List<Edge> edges, List<AccidentDto.Info> accidents) {
        List<WaypointDto> waypointDtos = new ArrayList<>();

        for (Edge edge : edges) {
            Waypoint waypoint = edge.getSrc();
            accidents.addAll(waypoint.getAccidents().stream().map(AccidentDto.Info::of).toList());
            waypointDtos.add(waypoint.toDto());
        }
        waypointDtos.add(edges.get(edges.size()-1).getDst().toDto());

        return waypointDtos;
    }
}
