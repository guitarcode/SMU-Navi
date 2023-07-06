package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.*;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.user.auth.Kind;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Accident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Kind kind;

    private String message;
    @ManyToOne
    private Waypoint waypoint;

}
