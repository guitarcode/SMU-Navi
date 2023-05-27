package smu.poodle.smnavi.map.domain.station;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.dto.WaypointDto;

import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//경유지점
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    String x;
    String y;

    @OneToMany(mappedBy = "startWaypoint")
    List<FullPath> fullPaths;

    public String getPointName(){
        return null;
    }
    public Waypoint(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public WaypointDto toDto(){
        return WaypointDto.builder()
                .gpsX(x)
                .gpsY(y)
                .build();
    }
}
