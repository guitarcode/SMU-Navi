package smu.poodle.smnavi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stations")
public class Station {
    @Id
    @Column(name = "station_id")
    private int id;

    private String name;
    private String number;
    private int index;

    private String startX;
    private String startY;
}
