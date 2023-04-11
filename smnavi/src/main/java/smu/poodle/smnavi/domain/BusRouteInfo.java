package smu.poodle.smnavi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Route route;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean isFull;
    private Integer congestion;

    private Integer time;

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setCongestion(Integer congestion) {
        this.congestion = congestion;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
