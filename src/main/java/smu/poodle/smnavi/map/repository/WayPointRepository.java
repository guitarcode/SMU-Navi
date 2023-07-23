package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import smu.poodle.smnavi.map.domain.station.Waypoint;

public interface WayPointRepository extends JpaRepository<Waypoint, Long> {

    @Query("select w from Waypoint as w where w.x = '126.955252' and w.y = '37.602638'")
    Waypoint getSmuWayPoint();
}
