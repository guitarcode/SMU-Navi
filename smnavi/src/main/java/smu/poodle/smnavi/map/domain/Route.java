package smu.poodle.smnavi.map.domain;

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
    private Long id;

    private Integer time;

    private String accidentInfo;

    private Boolean isSeen;

    @OneToMany(mappedBy = "route")
    List<RouteInfo> routeInfos;

    @ManyToOne
    @JoinColumn(name = "startStationId")
    private Station startStation;

    public void updateAccident(String accidentInfo){
        this.accidentInfo = accidentInfo;
    }

    public void updateIsSeen(){
        this.isSeen = !this.isSeen;
    }
}
