package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smu.poodle.smnavi.map.externapi.TransitType;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stationId")
    private Long id;

    private String localStationId;

    private String busName;
    private String stationName;


    @Enumerated(value = EnumType.STRING)
    private TransitType type;
    private String x;
    private String y;

    @OneToMany(mappedBy = "src")
    private List<Edge> nextEdges = new ArrayList<>();

    @OneToMany(mappedBy = "dst")
    private List<Edge> dstEdges = new ArrayList<>();


}
