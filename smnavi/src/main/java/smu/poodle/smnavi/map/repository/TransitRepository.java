package smu.poodle.smnavi.map.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.map.externapi.GpsPoint;
import smu.poodle.smnavi.map.domain.*;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransitRepository {

    private final EntityManager em;

    public List<StationInfo> findStationByLineNum(String lineName){
        return em.createQuery("select s from StationInfo as s " +
                "where s.lineName = :lineName", StationInfo.class)
                .setParameter("lineName", lineName)
                .getResultList();
    }

    public List<Station> findStationByLocalStationIdAndBusName(String localStationId, String busName){
        return em.createQuery("select s " +
                "from Station s " +
                "where s.localStationId = :stationId " +
                "and s.busName = :busName", Station.class)
                .setParameter("stationId", localStationId)
                .setParameter("busName", busName)
                .getResultList();
    }
    public List<Station> saveStations(List<Station> stationList){
        List<Station> persistedStationList = new ArrayList<>();
        for (Station station : stationList) {
            List<Station> findedStation = findStationByLocalStationIdAndBusName(station.getLocalStationId(), station.getBusName());
            if(findedStation.isEmpty()){
                em.persist(station);
                persistedStationList.add(station);
            }
            else{
                persistedStationList.add(findedStation.get(0));
            }
        }
        em.flush();

        return persistedStationList;
    }

    //쿼리
    public List<Edge> saveEdges(List<Edge> edgeList){
        List<Edge> persistedEdgeList = new ArrayList<>();
        for (Edge edge : edgeList) {
            List<Edge> sameEdge = em.createQuery("select e from Edge as e " +
                            "join fetch e.src s " +
                            "join fetch e.dst d " +
                            "where s.id = :startStationId and d.id = :endStationId", Edge.class)
                    .setParameter("startStationId", edge.getSrc().getId())
                    .setParameter("endStationId", edge.getDst().getId())
                    .getResultList();
            if(sameEdge.isEmpty()){
                em.persist(edge);
                persistedEdgeList.add(edge);
            }
            else{
                persistedEdgeList.add(sameEdge.get(0));
            }
        }

        em.flush();

        return persistedEdgeList;
    }

    public Route saveRoute(Station startStation, int time) {
        Route route = Route.builder()
                .time(time)
                .startStation(startStation)
                .isSeen(false)
                .build();
        em.persist(route);

        em.flush();
        return route;
    }

    public List<Route> findRouteByEdgeList(List<Edge> edgeList){
        List<Route> resultList = em.createQuery("select r.route " +
                        "from RouteInfo r " +
                        "inner join Edge as e on e = r.edge " +
                        "where e in :edges " +
                        "group by r.route.id " +
                        "having count(r.edge) = :num ", Route.class)
                .setParameter("edges", edgeList)
                .setParameter("num", edgeList.size())
                .getResultList();
        for (Route route : resultList) {
            List<RouteInfo> routeInfo = em.createQuery("select r " +
                            "from RouteInfo r " +
                            "where r.route = :route", RouteInfo.class)
                    .setParameter("route", route)
                    .getResultList();
            if(edgeList.size() == routeInfo.size()){
                return resultList;
            }
        }
        return new ArrayList<>();
    }

    public void saveRouteInfo(List<Edge> edgeList, Route route, boolean isMain){
        for (Edge edge : edgeList) {

            RouteInfo routeInfo = RouteInfo.builder()
                    .route(route)
                    .edge(edge)
                    .isMain(isMain)
                    .build();

            em.persist(routeInfo);
        }
        em.flush();
    }

//    public void saveRouteInfo(List<RouteInfo> routeInfoList){
//        for (RouteInfo routeInfo : routeInfoList) {
//            em.persist(routeInfo);
//        }
//
//        em.flush();
//    }



    public void saveDetailPositions(List<DetailPosition> detailPositionList) {
        for (DetailPosition detailPosition : detailPositionList) {
            em.persist(detailPosition);
        }
        em.flush();
    }

    public List<DetailPosition> findSimilarPoint(GpsPoint gpsPoint){
        return em.createQuery("select d from DetailPosition as d " +
                        "join fetch d.edge e " +
                        "join fetch e.src s " +
//                        "join e.routeInfoList info " +
//                        "join fetch info.route " +
                "where d.x like :x and d.y like :y", DetailPosition.class)
                .setParameter("x", "%"+gpsPoint.getGpsX()+"%")
                .setParameter("y", "%"+gpsPoint.getGpsY()+"%")
                .getResultList();
    }

    public List<DetailPosition> isContainDetailPos(Edge edge){
            return em.createQuery("select d from DetailPosition d " +
                                    "where d.edge = :edge", DetailPosition.class)
                            .setParameter("edge", edge)
                            .getResultList();

    }

    public List<Route> findAllRouteSeenTrue() {
        return em.createQuery("select r " +
                "from Route as r " +
                "join fetch r.startStation " +
                "where r.isSeen = TRUE", Route.class)
                .getResultList();
    }

    public Route findRouteById(Long routeId) {
        return em.createQuery("select r " +
                        "from Route as r " +
                        "join fetch r.startStation " +
                        "where r.id = : id ", Route.class)
                .setParameter("id", routeId)
                .getSingleResult();
    }

    public List<Route> findRouteByStartStationId(String startStationId) {
        return em.createQuery("select r " +
                        "from Route as r " +
                        "join fetch r.startStation " +
                        "where r.startStation.localStationId = :id ", Route.class)
                .setParameter("id", startStationId)
                .getResultList();
    }
    public List<RouteInfo> findRouteInfoByRoute(Route route) {
        return em.createQuery("select ri " +
                        "from RouteInfo as ri " +
                        "join fetch ri.edge as e " +
                        "join fetch e.src " +
                        "join fetch e.dst " +
                        "where ri.route = :route", RouteInfo.class)
                .setParameter("route", route)
                .getResultList();
    }


    public void saveBusRouteInfo(BusRouteInfo busRouteInfo) {
        em.persist(busRouteInfo);
    }
}
