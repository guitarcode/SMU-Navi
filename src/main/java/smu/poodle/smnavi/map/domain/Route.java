package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.poodle.smnavi.map.domain.station.Waypoint;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    Long id;

    Integer time;

    String accidentInfo;

    Boolean isSeen;

    @OneToMany(mappedBy = "route")
    List<RouteInfo> routeInfos;

    @ManyToOne
    @JoinColumn(name = "start_station_id")
    Waypoint startStation;

    public void updateAccident(String accidentInfo) {
        this.accidentInfo = accidentInfo;
    }

    public void updateIsSeen() {
        this.isSeen = !this.isSeen;
    }
}
