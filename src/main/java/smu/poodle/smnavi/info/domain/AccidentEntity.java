package smu.poodle.smnavi.info.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.user.auth.Kind;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccidentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Kind kind;

    @ManyToOne
    private Waypoint waypoint;

}
