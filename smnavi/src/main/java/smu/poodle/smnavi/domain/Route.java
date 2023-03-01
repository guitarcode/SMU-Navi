package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer id;

    private Integer time;

    private String accidentInfo;

    @OneToMany(mappedBy = "route")
    List<RouteInfo> routeInfos;

    @ManyToOne
    @JoinColumn(name = "startStationId")
    private Station startStation;

    public void updateAccident(){
        accidentInfo = "사고가 발생했습니다.";
    }
}
