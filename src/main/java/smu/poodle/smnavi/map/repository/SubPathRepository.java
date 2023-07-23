package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.domain.station.Waypoint;

import java.util.List;
import java.util.Optional;

public interface SubPathRepository extends JpaRepository<SubPath, Long> {

    Optional<SubPath> findTopBySrcAndDst(Waypoint src, Waypoint dst);
}
