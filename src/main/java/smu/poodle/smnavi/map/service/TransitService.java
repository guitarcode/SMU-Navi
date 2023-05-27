package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.dto.RouteDto;
import smu.poodle.smnavi.map.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static smu.poodle.smnavi.map.util.ConvertDtoUtil.convertRouteInfoToDto;

@Service
@Transactional
@RequiredArgsConstructor
public class TransitService {

    private final TransitRepository transitRepository;


    public List<PathDto.Info> getTransitRoute(String startStationId) {
        List<PathDto.Info> pathInfoList = new ArrayList<>();

        List<Route> routeList = transitRepository.findRouteByStartStationId(startStationId);

        for (Route route : routeList) {
            List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
            PathDto.Info pathDto = convertRouteInfoToDto(routeInfoList, route.getTime());
            pathInfoList.add(pathDto);
        }


        return pathInfoList;
    }

    public List<RouteDto.Info> getRouteList() {
        List<Route> routeList = transitRepository.findAllRouteSeenTrue();

        return routeList.stream().map(RouteDto.Info::new).collect(Collectors.toList());
    }

    public void updateRouteSeen(Long id) {
        Route route = transitRepository.findRouteById(id);

        route.updateIsSeen();
    }
}
