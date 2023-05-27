package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.path.SubPath;
public interface SubPathRepository extends JpaRepository<SubPath, Long> {

}
