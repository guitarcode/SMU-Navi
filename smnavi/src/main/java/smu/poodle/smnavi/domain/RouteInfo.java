package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RouteInfo {

    //id 정렬만 해줘도 루트가 순서대로 올까?
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_info_id")
    private Integer id;

    private boolean isMain;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Edge edge;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Route route;
}
