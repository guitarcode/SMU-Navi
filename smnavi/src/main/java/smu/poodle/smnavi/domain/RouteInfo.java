package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfo {

    //id 정렬만 해줘도 루트가 순서대로 올까?
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_info_id")
    private Integer id;

    @ManyToOne
    private Edge edge;

    @ManyToOne
    private Route route;
}
