package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;

import java.util.List;

public interface FullPathAndSubPathRepository extends JpaRepository<FullPathAndSubPath, Long> {

    @Query("select f from FullPath f " +
            "join FullPathAndSubPath as fullAndSubMapping " +
            "on f = fullAndSubMapping.fullPath " +
            "where count(f) = :subPathSize " +
            "group by f " +
            "having fullAndSubMapping.subPath in :subPathList")
    List<FullPath> findAllBySubPath(@Param("subPathList") List<SubPath> subPathList
            , @Param("subPathSize") int subPathSize);
}
