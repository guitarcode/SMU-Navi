package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.dto.RouteDto;
import smu.poodle.smnavi.map.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PathService {

    private final TransitRepository transitRepository;
    private final SubwayStationRepository subwayStationRepository;
    private final BusStationRepository busStationRepository;


    public List<PathDto.Info> getTransitRoute(String startStationId) {
        List<PathDto.Info> pathInfoList = new ArrayList<>();

        List<Waypoint> waypoints;
        if (startStationId.length() < 5) {
            waypoints = subwayStationRepository.findAllByStationId(Integer.parseInt(startStationId));
        } else {
            waypoints = busStationRepository.findAllByLocalStationId(startStationId);
        }

        List<FullPath> fullPaths = new ArrayList<>();

        for (Waypoint waypoint : waypoints) {
            fullPaths.addAll(waypoint.getFullPaths());
        }

        for (FullPath fullPath : fullPaths) {
            PathDto.Info pathDto = PathDto.Info.fromEntity(fullPath);
            pathInfoList.add(pathDto);
        }

        return pathInfoList;
    }

    public List<RouteDto.Info> getRouteList() {
        List<FullPath> fullPathList = transitRepository.findAllRouteSeenTrue();

        //todo: fullpath가 가지고 있는 startstation의 목록을 반환해주는 쿼리를 작성해보기
        List<Waypoint> waypoints = fullPathList.stream().map(FullPath::getStartWaypoint).distinct().toList();
        return waypoints.stream().map(RouteDto.Info::new).collect(Collectors.toList());
    }

    public void updateRouteSeen(Long id) {
        FullPath fullPath = transitRepository.findRouteById(id);

        fullPath.updateIsSeen();
    }
}
