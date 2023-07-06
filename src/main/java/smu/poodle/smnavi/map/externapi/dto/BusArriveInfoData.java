package smu.poodle.smnavi.map.externapi.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusArriveInfoData {

    int stationId;
    int stationOrder;
    int firstArrivalSeconds;
    int secondArrivalSeconds;
    int firstArrivalStationOrder;
    int secondArrivalStationOrder;
    boolean isStationNonStop;

}
