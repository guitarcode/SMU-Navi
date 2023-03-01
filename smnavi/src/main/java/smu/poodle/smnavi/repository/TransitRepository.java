package smu.poodle.smnavi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.domain.*;
import smu.poodle.smnavi.externapi.GpsPoint;

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

    public void saveStations(List<Station> stationList){
        for (Station station : stationList) {
            Station findedStation = em.find(Station.class, station.getId());
            if(findedStation == null){
                em.persist(station);
            }
        }
        em.flush();
    }

    //쿼리
    public List<Edge> saveEdges(List<Edge> edgeList){
        boolean isNewRoute = false;
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
                isNewRoute = true;
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
                .build();
        em.persist(route);

        em.flush();
        return route;
    }

    public void saveRouteInfo(List<Edge> edgeList, Route route){
        for (Edge edge : edgeList) {

            RouteInfo routeInfo = RouteInfo.builder()
                    .route(route)
                    .edge(edge)
                    .build();

            em.persist(routeInfo);
        }
        em.flush();
    }

//    private List<Edge> findNextEdge(Station dstStation){
//        return em.createQuery("select e from Edge as e " +
//                        "join fetch e.src s " +
//                        "join fetch e.dst d " +
//                        "where s.id = :dstId", Edge.class)
//                .setParameter("dstId", dstStation.getId())
//                .getResultList();
//    }
//
//    private List<Edge> findFirstEdge(Station dstStation){
//        return em.createQuery("select e from Edge as e " +
//                        "join fetch e.src s " +
//                        "join fetch e.dst d " +
//                        "where d.id = :dstId", Edge.class)
//                .setParameter("dstId", dstStation.getId())
//                .getResultList();
//    }

//    private boolean saveEdgeWhenExternalBranch(List<Edge> edgeList, boolean isNewRoute){
//        int idx;
//        for (idx = 0; idx < edgeList.size(); idx++) {
//            Edge edge = edgeList.get(idx);
//            List<Edge> resultList = findNextEdge(edge.getDst());
//            if (!resultList.isEmpty()) {
//                edge.setDst(resultList.get(0).getSrc());
//                em.persist(edge);
//                break;
//            } else {
//                em.persist(edge);
//                isNewRoute = true;
//            }
//        }
//        if(idx < edgeList.size()) {
//            isNewRoute = saveEdgeWhenInternalBranch(edgeList.subList(idx, edgeList.size()), isNewRoute);
//        }
//
//        em.flush();
//        return isNewRoute;
//    }

//    private boolean saveEdgeWhenInternalBranch(List<Edge> edgeList, boolean isNewRoute){
//        int idx;
//        for (idx = 0; idx < edgeList.size(); idx++) {
//            Edge edge = edgeList.get(idx);
//            List<Edge> resultList = findFirstEdge(edge.getDst());
//            if(resultList.isEmpty())
//                break;
//            else{
//                edgeList.set(idx, resultList.get(0));
//            }
//        }
//
//        if(idx < edgeList.size()) {
//            isNewRoute = saveEdgeWhenExternalBranch(edgeList.subList(idx, edgeList.size()), isNewRoute);
//        }
//
//        return isNewRoute;
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

//    public List<Edge> findAllEdgeById(List<Long> id){
//        return em.createQuery("select e from Edge e " +
//                "where ");
//    }
}
