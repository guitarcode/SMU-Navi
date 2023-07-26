package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import smu.poodle.smnavi.common.BaseTimeEntity;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.user.auth.Kind;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "updated_at > curdate()")
@Getter
public class Accident extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Kind kind;

    private String message;
    @ManyToOne
    private Waypoint waypoint;
}
