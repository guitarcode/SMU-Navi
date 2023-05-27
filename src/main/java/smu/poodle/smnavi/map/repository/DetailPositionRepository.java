package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.path.DetailPosition;

public interface DetailPositionRepository extends JpaRepository<DetailPosition, Long> {
}
