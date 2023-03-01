package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "edges")
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edge_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_id")
    private Station src;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dst_id")
    private Station dst;

    @OneToMany(mappedBy = "edge")
    private List<RouteInfo> routeInfoList;

    public void setDst(Station dst) {
        this.dst = dst;
    }
}
