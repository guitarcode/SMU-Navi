package smu.poodle.smnavi.map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import smu.poodle.smnavi.map.dto.TransitPathDto;
import smu.poodle.smnavi.map.odsay.TransitRouteApi;
import smu.poodle.smnavi.map.externapi.accident.AccidentApi;
import smu.poodle.smnavi.map.response.BaseResponse;
import smu.poodle.smnavi.map.response.TransitResponse;
import smu.poodle.smnavi.map.service.DeprecatedService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeprecatedController {

    private final TransitRouteApi transitRouteApi;

    private final DeprecatedService deprecatedService;
    private final AccidentApi accidentApi;

    //근처 위치에서 학교를 가는 경로를 찾는 API (사용 안함)
    @GetMapping("/api/map/transit")
    public ResponseEntity<BaseResponse> findTransit(@RequestParam String startX, @RequestParam String startY){

        List<TransitPathDto> transitRoute = deprecatedService.getTransitRoute(startX, startY);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .build();

        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }

    @GetMapping("/api/test/accident")
    public ResponseEntity<HttpStatus> getGps() throws ParserConfigurationException, IOException, SAXException {

        accidentApi.getAccidentInfo();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
