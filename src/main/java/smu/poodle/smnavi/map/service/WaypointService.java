package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.station.BusStation;
import smu.poodle.smnavi.map.domain.station.SubwayStation;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.repository.BusStationRepository;
import smu.poodle.smnavi.map.repository.SubwayStationRepository;
import smu.poodle.smnavi.map.repository.WayPointRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaypointService {
    private final WayPointRepository wayPointRepository;
    private final BusStationRepository busStationRepository;
    private final SubwayStationRepository subwayStationRepository;

    public Waypoint save(Waypoint waypoint) {
        Optional<? extends Waypoint> optionalWaypoint = findWaypoint(waypoint);

        if(optionalWaypoint.isPresent()){
            return optionalWaypoint.get();
        }
        else{
            return wayPointRepository.save(waypoint);
        }
    }

    /**
     * Waypoint 의 필드를 바탕으로 db에 있는지 여부를 반환
     *
     * @return 존재하는 지점이면 저장된 waypoint 반환
     * 존재하지 않는 지점이거나 optional.empty 반환
     */
    private Optional<? extends Waypoint> findWaypoint(Waypoint waypoint) {
        if (waypoint instanceof BusStation) {
            return findBusStation((BusStation) waypoint);
        } else if (waypoint instanceof SubwayStation) {
            return findSubwayStation((SubwayStation) waypoint);
        }
        return Optional.empty();
    }

    private Optional<? extends Waypoint> findBusStation(BusStation busStation) {
        return busStationRepository.findFirstByLocalStationIdAndBusName(
                busStation.getLocalStationId(),
                busStation.getBusName());
    }

    private Optional<? extends Waypoint> findSubwayStation(SubwayStation subwayStation) {
        return subwayStationRepository.findFirstByLineNameAndStationId(
                        subwayStation.getLineName(),
                        subwayStation.getStationId());
    }

}
