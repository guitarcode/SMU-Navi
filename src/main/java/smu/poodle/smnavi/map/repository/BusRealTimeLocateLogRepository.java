package smu.poodle.smnavi.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smu.poodle.smnavi.map.domain.BusRealTimeLocationInfo;
import smu.poodle.smnavi.map.domain.BusRealTimeLocationLog;

import java.util.Optional;
import java.util.Set;

public interface BusRealTimeLocateLogRepository extends JpaRepository<BusRealTimeLocationLog, Long> {
}
