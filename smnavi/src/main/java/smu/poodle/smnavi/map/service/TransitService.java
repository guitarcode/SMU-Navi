package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.map.domain.Edge;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.domain.StationInfo;
import smu.poodle.smnavi.map.dto.RouteDto;
import smu.poodle.smnavi.map.dto.RouteInfoDto;
import smu.poodle.smnavi.map.dto.TransitPathDto;
import smu.poodle.smnavi.map.repository.TransitRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static smu.poodle.smnavi.map.util.ConvertDtoUtil.convertRouteInfoToDto;

@Service
@Transactional
@RequiredArgsConstructor
public class TransitService {

    private final TransitRepository transitRepository;




    public List<TransitPathDto> getTransitRoute(String startStationId) {
        List<TransitPathDto> pathInfoList = new ArrayList<>();

        List<Route> routeList = transitRepository.findRouteByStartStationId(startStationId);

        for (Route route : routeList) {
            List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
            TransitPathDto transitPathDto = convertRouteInfoToDto(routeInfoList, route.getTime());
            pathInfoList.add(transitPathDto);
        }


        return pathInfoList;
    }

    public List<RouteDto> getRouteList() {
        List<Route> routeList = transitRepository.findAllRouteSeenTrue();

        return routeList.stream().map(RouteDto::new).collect(Collectors.toList());
    }

    public void updateRouteSeen(Long id) {
        Route route = transitRepository.findRouteById(id);

        route.updateIsSeen();
    }
}
