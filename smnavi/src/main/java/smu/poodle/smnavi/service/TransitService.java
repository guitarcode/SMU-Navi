package smu.poodle.smnavi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.SubwayStationInfo;
import smu.poodle.smnavi.repository.TransitRepository;
import smu.poodle.smnavi.response.TransitResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransitService {

    private final TransitRepository transitRepository;

    public Map<Integer, SubwayStationInfo> stationInfoMap(String lineName){
        List<SubwayStationInfo> stationInfoList = transitRepository.findStationByLineNum(lineName);
        Map<Integer,SubwayStationInfo> stationInfoMap = new HashMap<>();
        for (SubwayStationInfo stationInfo : stationInfoList) {
            stationInfoMap.put(stationInfo.getStationNum(), stationInfo);
        }
//        stationInfoList.stream().collect(Collectors.toMap(SubwayStationInfo::getStationId, ));
        return stationInfoMap;
    }
}
