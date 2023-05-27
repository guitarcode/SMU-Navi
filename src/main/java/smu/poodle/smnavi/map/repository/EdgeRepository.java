package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.poodle.smnavi.map.domain.path.Edge;

import java.util.List;
import java.util.Optional;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    public Optional<Edge> findFirstBySrcIdAndDstId(Long srcId, Long dstId);
}
