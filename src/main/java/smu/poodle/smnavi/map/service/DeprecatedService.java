package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Deprecated
public class DeprecatedService {
//    private final TransitRepository transitRepository;
//
//    public List<PathDto> getTransitRoute(String startX, String startY) {
//        List<PathDto> pathInfoList = new ArrayList<>();
//
//        List<Route> routeList = transitRepository.findAllRouteSeenTrue();
//        GpsPoint gpsPoint = new GpsPoint(startX, startY);
//
//        for (Route route : routeList) {
//            GpsPoint startPoint = new GpsPoint(route.getStartStation().getX(), route.getStartStation().getY());
//            double distance = ApiUtilMethod.calculateDistance(gpsPoint, startPoint);
//            if(distance < 300){
//                List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
//                PathDto pathDto = convertRouteInfoToDto(routeInfoList, route.getTime());
//                pathInfoList.add(pathDto);
//            }
//        }
//
//        return pathInfoList;
//    }
//
//    public List<PathDto.RouteInfoDto> getRoute(Long id) {
//        Route route = transitRepository.findRouteById(id);
//        List<RouteInfo> routeInfoList = transitRepository.findRouteInfoByRoute(route);
//
//        List<PathDto.RouteInfoDto> routeInfoDto = new ArrayList<>();
//        for (int i = 1; i <= routeInfoList.size(); i++) {
//            routeInfoDto.add(new PathDto.RouteInfoDto(i,routeInfoList.get(i-1)));
//        }
//
//        return routeInfoDto;
//    }
}
