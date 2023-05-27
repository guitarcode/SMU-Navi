package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.station.Waypoint;

public interface WayPointRepository extends JpaRepository<Waypoint, Long> {
}
