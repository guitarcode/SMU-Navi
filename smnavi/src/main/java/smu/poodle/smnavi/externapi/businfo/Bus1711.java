package smu.poodle.smnavi.externapi.businfo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import smu.poodle.smnavi.domain.Route;
import smu.poodle.smnavi.repository.TransitRepository;

@RequiredArgsConstructor
@Service
@Transactional

public class Bus1711 {

    private final BusInfoApi busInfoApi;
    private final TransitRepository transitRepository;

    private final String busId = "100100447";
    private final String NAMYEONG_X = "126.9716794679";
    private final String NAMYEONG_Y = "37.5416932813";

    private final String SEOUL_X = "126.9729705525";
    private final String SEOUL_Y = "37.555613815";
    private final String SICHEONG_X = "126.976551";
    private final String SICHEONG_Y = "37.562214";

    public void createInfo(){

        createNamyeongInfo();

        createSeoulStationInfo();

        createSicheongInfo();
    }

    public void createNamyeongInfo(){

        Document xmlContent = busInfoApi.getBusPosInfo(busId, 38, 41);

        int time = busInfoApi.routeTime(NAMYEONG_X, NAMYEONG_Y, 0);

        Route route = transitRepository.findRouteById(1L);

        busInfoApi.parsingBusPosInfo(xmlContent, time, route);

    }

    public void createSeoulStationInfo(){

        Document xmlContent = busInfoApi.getBusPosInfo(busId, 41, 44);

        int time = busInfoApi.routeTime(SEOUL_X, SEOUL_Y, 1);

        Route route = transitRepository.findRouteById(4L);

        busInfoApi.parsingBusPosInfo(xmlContent, time, route);

    }

    public void createSicheongInfo(){

        Document xmlContent = busInfoApi.getBusPosInfo(busId, 44, 45);

        int time = busInfoApi.routeTime(SICHEONG_X, SICHEONG_Y, 0);

        Route route = transitRepository.findRouteById(5L);

        busInfoApi.parsingBusPosInfo(xmlContent, time, route);

    }


}
