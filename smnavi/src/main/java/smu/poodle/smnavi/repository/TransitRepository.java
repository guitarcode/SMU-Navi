package smu.poodle.smnavi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.domain.Edge;
import smu.poodle.smnavi.domain.StationInfo;

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

    public void saveEdges(List<Edge> edgeList){
        for (Edge edge : edgeList) {
            em.persist(edge);
        }
    }
}
