package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;

public interface FullPathAndSubPathRepository extends JpaRepository<FullPathAndSubPath, Long> {
}
