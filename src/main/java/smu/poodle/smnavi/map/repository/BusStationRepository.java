package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.map.domain.station.BusStation;
import smu.poodle.smnavi.map.domain.station.SubwayStation;
import smu.poodle.smnavi.map.domain.station.Waypoint;

import java.util.List;
import java.util.Optional;

public interface BusStationRepository extends JpaRepository<BusStation, Long> {
    Optional<BusStation> findFirstByLocalStationIdAndBusName(String localStationId, String busName);

    List<Waypoint> findAllByLocalStationId(String localStationId);

}
