package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.path.FullPath;


public interface FullPathRepository extends JpaRepository<FullPath, Long> {
}

