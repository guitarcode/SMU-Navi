package smu.poodle.smnavi.info.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import smu.poodle.smnavi.map.domain.data.TransitType;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.user.auth.Kind;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    @CreationTimestamp
    private LocalDateTime regDate;
    @Column
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column
    private Kind kind;
    @Column
    private Location location;
    @Column
    private int increaseCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Waypoint waypoint;

    @Enumerated(EnumType.STRING)
    private TransitType transitType;
    public void increaseViews(){
        this.increaseCount++;
    }

}
