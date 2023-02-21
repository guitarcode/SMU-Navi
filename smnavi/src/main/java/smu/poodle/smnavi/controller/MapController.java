package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import smu.poodle.smnavi.externapi.GpsPoint;
import smu.poodle.smnavi.externapi.TransitPathDto;
import smu.poodle.smnavi.externapi.TransitRouteApi;
import smu.poodle.smnavi.externapi.accident.AccidentApi;
import smu.poodle.smnavi.response.BaseResponse;
import smu.poodle.smnavi.response.TransitResponse;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final TransitRouteApi transitRouteApi;
    private final AccidentApi accidentApi;

    @GetMapping("/api/map/transit")
    public ResponseEntity<BaseResponse> findTransit(@RequestParam String startX, @RequestParam String startY){

        List<TransitPathDto> transitRoute = transitRouteApi.getTransitRoute(startX, startY);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .build();

        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }

    @GetMapping("/api/test")
    public ResponseEntity<HttpStatus> getGps() throws ParserConfigurationException, IOException, SAXException {
        accidentApi.getAccidentInfo();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

