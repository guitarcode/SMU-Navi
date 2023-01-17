package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(name = "stationId")
    private String id;

    private String name;

    private String x;
    private String y;

    @OneToMany(mappedBy = "src")
    private List<Edge> nextEdges = new ArrayList<>();

    @OneToMany(mappedBy = "dst")
    private List<Edge> dstEdges = new ArrayList<>();


}
