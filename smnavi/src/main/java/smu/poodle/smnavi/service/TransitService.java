package smu.poodle.smnavi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.StationInfo;
import smu.poodle.smnavi.repository.TransitRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TransitService {

    private final TransitRepository transitRepository;

    public Map<Integer, StationInfo> stationInfoMap(String lineName){
        List<StationInfo> stationInfoList = transitRepository.findStationByLineNum(lineName);
        Map<Integer, StationInfo> stationInfoMap = new HashMap<>();
        for (StationInfo stationInfo : stationInfoList) {
            stationInfoMap.put(stationInfo.getStationNum(), stationInfo);
        }
//        stationInfoList.stream().collect(Collectors.toMap(SubwayStationInfo::getStationId, ));
        return stationInfoMap;
    }

}
