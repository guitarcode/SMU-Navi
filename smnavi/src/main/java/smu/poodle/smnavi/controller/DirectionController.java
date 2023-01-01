package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smu.poodle.smnavi.callapi.GetBusAndSubwayAPI;
import smu.poodle.smnavi.callapi.GpsPoint;
import smu.poodle.smnavi.callapi.PathInfo;
import smu.poodle.smnavi.response.TransitResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DirectionController {

    private final GetBusAndSubwayAPI getBusAndSubwayAPI;

    @GetMapping("/api/map/transit")
    public TransitResponse findTransit(@RequestParam String startX,@RequestParam String startY){
        // api/map/transit?startX=127.123213213&startY=37.123123
        List<PathInfo> transitRoute = getBusAndSubwayAPI.getTransitRoute(new GpsPoint(startX, startY));
        return new TransitResponse("200", transitRoute);
    }

}
