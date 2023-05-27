package smu.poodle.smnavi.map.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import smu.poodle.smnavi.map.domain.path.FullPath;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusRouteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    FullPath fullPath;

    @CreationTimestamp
    LocalDateTime createdAt;

    Boolean isFull;
    Integer congestion;
    Integer time;

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setCongestion(Integer congestion) {
        this.congestion = congestion;
    }

    public void setFullPath(FullPath fullPath) {
        this.fullPath = fullPath;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
