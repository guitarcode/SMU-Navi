package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.poodle.smnavi.map.domain.station.Waypoint;

import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "edges")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edge_id")
    Integer id;

    Boolean detailExist;

    Integer walkingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_id")
    Waypoint src;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dst_id")
    Waypoint dst;

    @OneToMany(mappedBy = "edge")
    List<RouteInfo> routeInfoList;

    @OneToMany(mappedBy = "edge", cascade = CascadeType.ALL)
    List<DetailPosition> detailPositionList;

    public void setDetailExistTrue() {
        this.detailExist = true;
    }

    public void setDetailPositionList(List<DetailPosition> detailPositionList) {
        this.detailPositionList = detailPositionList;
    }
}
