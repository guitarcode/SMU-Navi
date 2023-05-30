package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.Accident;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
}
