package smu.poodle.smnavi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.domain.*;

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
    public boolean saveEdges(List<Edge> edgeList){
        boolean isNewRoute = false;
        Station startStation = edgeList.get(0).getSrc();
        List<Edge> sameStartEdgeList = em.createQuery("select e from Edge as e " +
                        "join fetch e.src s " +
                        "join fetch e.dst d " +
                        "where s.id = :startStationId", Edge.class)
                .setParameter("startStationId", startStation.getId())
                .getResultList();

        if (sameStartEdgeList.isEmpty()) {
            isNewRoute = saveEdgeWhenExternalBranch(edgeList, isNewRoute);
        }
        else {
            isNewRoute = saveEdgeWhenInternalBranch(edgeList, isNewRoute);
        }

        em.flush();

        return isNewRoute;
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

    private List<Edge> findNextEdge(Station dstStation){
        return em.createQuery("select e from Edge as e " +
                        "join fetch e.src s " +
                        "join fetch e.dst d " +
                        "where s.id = :dstId", Edge.class)
                .setParameter("dstId", dstStation.getId())
                .getResultList();
    }

    private List<Edge> findFirstEdge(Station dstStation){
        return em.createQuery("select e from Edge as e " +
                        "join fetch e.src s " +
                        "join fetch e.dst d " +
                        "where d.id = :dstId", Edge.class)
                .setParameter("dstId", dstStation.getId())
                .getResultList();
    }

    private boolean saveEdgeWhenExternalBranch(List<Edge> edgeList, boolean isNewRoute){
        int idx;
        for (idx = 0; idx < edgeList.size(); idx++) {
            Edge edge = edgeList.get(idx);
            List<Edge> resultList = findNextEdge(edge.getDst());
            if (!resultList.isEmpty()) {
                edge.setDst(resultList.get(0).getSrc());
                em.persist(edge);
                break;
            } else {
                em.persist(edge);
                isNewRoute = true;
            }
        }
        if(idx < edgeList.size()) {
            isNewRoute = saveEdgeWhenInternalBranch(edgeList.subList(idx, edgeList.size()), isNewRoute);
        }


        em.flush();
        return isNewRoute;
    }

    private boolean saveEdgeWhenInternalBranch(List<Edge> edgeList, boolean isNewRoute){
        int idx;
        for (idx = 0; idx < edgeList.size(); idx++) {
            Edge edge = edgeList.get(idx);
            List<Edge> resultList = findFirstEdge(edge.getDst());
            if(resultList.isEmpty())
                break;
            else{
                edgeList.set(idx, resultList.get(0));
            }
        }

        if(idx < edgeList.size()) {
            isNewRoute = saveEdgeWhenExternalBranch(edgeList.subList(idx, edgeList.size()), isNewRoute);
        }

        return isNewRoute;
    }
}
