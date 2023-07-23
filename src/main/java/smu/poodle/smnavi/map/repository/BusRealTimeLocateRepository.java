package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import smu.poodle.smnavi.map.domain.BusRealTimeLocationInfo;

import java.util.Optional;
import java.util.Set;

public interface BusRealTimeLocateRepository extends JpaRepository<BusRealTimeLocationInfo, Long> {
    Optional<BusRealTimeLocationInfo> findByLicensePlate(String licensePlate);


    @Modifying
    @Query("delete from BusRealTimeLocationInfo b where b.licensePlate not in :keySet")
    void deleteAllOutOfBoundBusInfo(Set<String> licensePlateSet);
}
