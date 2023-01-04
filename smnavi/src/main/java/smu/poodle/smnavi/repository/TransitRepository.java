package smu.poodle.smnavi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.domain.SubwayStationInfo;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransitRepository {

    private final EntityManager em;

    public List<SubwayStationInfo> findStationByLineNum(String lineName){
        return em.createQuery("select s from SubwayStationInfo as s " +
                "where s.lineName = :lineName", SubwayStationInfo.class)
                .setParameter("lineName", lineName)
                .getResultList();
    }
}
