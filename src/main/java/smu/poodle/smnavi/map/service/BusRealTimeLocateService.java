package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.dto.BusArriveInfoDto;
import smu.poodle.smnavi.map.dto.BusRealTimeLocationDto;
import smu.poodle.smnavi.map.repository.AccidentRepository;
import smu.poodle.smnavi.map.repository.BusRealTimeLocateLogRepository;
import smu.poodle.smnavi.map.repository.BusRealTimeLocateRepository;
import smu.poodle.smnavi.map.repository.BusStationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusRealTimeLocateService {
    private final BusRealTimeLocateRepository busRealTimeLocateRepository;
    private final BusRealTimeLocateLogRepository busRealTimeLocateLogRepository;
    private final AccidentRepository accidentRepository;
    private final BusStationRepository busStationRepository;

    @Transactional
    public void checkTrafficErrorByBusMovement(List<BusArriveInfoDto> busArriveInfoDtoList) {
        Map<String, BusRealTimeLocationDto> busRealTimeLocationDtoMap = parseMapFromDto(busArriveInfoDtoList);

        for (String licensePlate : busRealTimeLocationDtoMap.keySet()) {
            BusRealTimeLocationDto busRealTimeLocationDto = busRealTimeLocationDtoMap.get(licensePlate);
            busRealTimeLocateLogRepository.save(busRealTimeLocationDto.toLogEntity("7016"));
            busRealTimeLocateRepository.findByLicensePlate(licensePlate).ifPresentOrElse(
                    busRealTimeLocateInfo -> {
                        if (busRealTimeLocateInfo.getStationOrder() == busRealTimeLocationDto.getStationOrder()) {
                            List<Waypoint> busStation = busStationRepository.findAllByLocalStationId(busRealTimeLocationDto.getStationId());

                            if (!busStation.isEmpty()) {
                                accidentRepository.save(busRealTimeLocationDto.toAccidentEntity(busStation.get(0)));
                            }
                        } else {
                            busRealTimeLocateInfo.setStationId(busRealTimeLocationDto.getStationId());
                            busRealTimeLocateInfo.setStationOrder(busRealTimeLocationDto.getStationOrder());
                        }
                    },
                    () -> busRealTimeLocateRepository.save(busRealTimeLocationDto.toInfoEntity("7016"))
            );
        }

        busRealTimeLocateRepository.deleteAllOutOfBoundBusInfo(busRealTimeLocationDtoMap.keySet());

    }

    private Map<String, BusRealTimeLocationDto> parseMapFromDto(List<BusArriveInfoDto> busArriveInfoDtoList) {
        Map<String, BusRealTimeLocationDto> busRealTimeLocationDtoMap = new HashMap<>();
        for (BusArriveInfoDto busArriveInfoDto : busArriveInfoDtoList) {

            String firstArrivalLicensePlate = busArriveInfoDto.getFirstArrivalLicensePlate();
            String secondArrivalLicensePlate = busArriveInfoDto.getSecondArrivalLicensePlate();


            busRealTimeLocationDtoMap.putIfAbsent(
                    firstArrivalLicensePlate,
                    BusRealTimeLocationDto.builder()
                            .licensePlate(firstArrivalLicensePlate)
                            .stationOrder(busArriveInfoDto.getFirstArrivalStationOrder())
                            .stationId(busArriveInfoDto.getFirstArrivalNextStationId())
                            .build());

            busRealTimeLocationDtoMap.putIfAbsent(
                    secondArrivalLicensePlate,
                    BusRealTimeLocationDto.builder()
                            .licensePlate(secondArrivalLicensePlate)
                            .stationOrder(busArriveInfoDto.getSecondArrivalStationOrder())
                            .stationId(busArriveInfoDto.getSecondArrivalNextStationId())
                            .build());
        }

        return busRealTimeLocationDtoMap;
    }
}
