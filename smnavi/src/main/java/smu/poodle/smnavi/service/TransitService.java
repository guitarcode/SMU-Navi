package smu.poodle.smnavi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.Edge;
import smu.poodle.smnavi.domain.Route;
import smu.poodle.smnavi.domain.RouteInfo;
import smu.poodle.smnavi.domain.StationInfo;
import smu.poodle.smnavi.dto.RouteDto;
import smu.poodle.smnavi.dto.RouteInfoDto;
import smu.poodle.smnavi.repository.TransitRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransitService {

    private final TransitRepository transitRepository;

    public Map<Integer, StationInfo> stationInfoMap(String lineName){
        List<StationInfo> stationInfoList = transitRepository.findStationByLineNum(lineName);
        Map<Integer, StationInfo> stationInfoMap = new HashMap<>();
        for (StationInfo stationInfo : stationInfoList) {
            stationInfoMap.put(stationInfo.getStationNum(), stationInfo);
        }
//        stationInfoList.stream().collect(Collectors.toMap(SubwayStationInfo::getStationId, ));
        return stationInfoMap;
    }

    public void linkRoute(Long routeId1, int infoId1, Long routeId2, int infoId2){
        Route route1 = transitRepository.findRouteById(routeId1);
        Route route2 = transitRepository.findRouteById(routeId2);

        List<RouteInfo> routeInfoList1 = transitRepository.findRouteInfoByRoute(route1);
        List<RouteInfo> routeInfoList2 = transitRepository.findRouteInfoByRoute(route2);

        List<Edge> entireEdgeList = new ArrayList<>();

        for (int i = 0; i < infoId1; i++) {
            RouteInfo routeInfo = routeInfoList1.get(i);
            entireEdgeList.add(routeInfo.getEdge());
        }

        for (int i = infoId2; i < routeInfoList2.size() - infoId2 + 1; i++) {
            RouteInfo routeInfo = routeInfoList1.get(i);
            entireEdgeList.add(routeInfo.getEdge());
        }

        int size = entireEdgeList.size();
        int time = route1.getTime();
        boolean isMain = true;
        for (int j = 0; j < size; j++) {
            if (transitRepository.findRouteByEdgeList(entireEdgeList).isEmpty()) {
                Route route = transitRepository.saveRoute(entireEdgeList.get(0).getSrc(), time);
                transitRepository.saveRouteInfo(entireEdgeList, route, isMain);
                isMain = false;
                entireEdgeList.remove(0);
                time = time >= 3 ? time - 3 : 0;
            }
            else {
                break;
            }
        }

    }

    public List<RouteInfoDto> getRoute(Long id) {
        Route route = transitRepository.findRouteById(id);
        List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);

        List<RouteInfoDto> routeInfoDto = new ArrayList<>();
        for (int i = 1; i <= routeInfoList.size(); i++) {
            routeInfoDto.add(new RouteInfoDto(i,routeInfoList.get(i-1)));
        }

        return routeInfoDto;
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
