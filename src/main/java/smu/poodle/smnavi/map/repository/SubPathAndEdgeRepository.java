package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.mapping.SubPathAndEdge;
public interface SubPathAndEdgeRepository extends JpaRepository<SubPathAndEdge, Long> {
}
