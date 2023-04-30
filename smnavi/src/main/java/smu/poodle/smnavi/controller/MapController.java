package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import smu.poodle.smnavi.dto.RouteDto;
import smu.poodle.smnavi.externapi.TransitPathDto;
import smu.poodle.smnavi.externapi.TransitRouteApi;
import smu.poodle.smnavi.externapi.accident.AccidentApi;
import smu.poodle.smnavi.response.BaseResponse;
import smu.poodle.smnavi.response.TransitResponse;
import smu.poodle.smnavi.service.TransitService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final TransitRouteApi transitRouteApi;
    private final TransitService transitService;
    private final AccidentApi accidentApi;

    //odsay api 호출해서 api 만드는 요청
    @PostMapping("/api/map/transit")
    public ResponseEntity<BaseResponse> saveTransit(@RequestParam String startX, @RequestParam String startY
    ,@RequestParam String numbers){

        List<TransitPathDto> transitRoute = transitRouteApi.getTransitRoute(startX, startY, numbers);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .build();

        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }

    //
    @GetMapping("/api/map/transit")
    public ResponseEntity<BaseResponse> findTransit(@RequestParam String startX, @RequestParam String startY){

        List<TransitPathDto> transitRoute = transitRouteApi.getTransitRoute(startX, startY);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .build();

        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }


    @GetMapping("/api/route/{startStationId}")
    public ResponseEntity<BaseResponse> getRoute(@PathVariable String startStationId){

        List<TransitPathDto> transitRoute = transitRouteApi.getTransitRoute(startStationId);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .pathInfoCnt(transitRoute.size())
                .build();


        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }
    @GetMapping("/api/test")
    public ResponseEntity<HttpStatus> getGps() throws ParserConfigurationException, IOException, SAXException {

        accidentApi.getAccidentInfo();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/route")
    public ResponseEntity<List<RouteDto>> getRouteList(){
        List<RouteDto> routeList = transitService.getRouteList();

        return new ResponseEntity<>(routeList, HttpStatus.OK);
    }

    @PostMapping("/api/route/seen/{id}")
    public ResponseEntity<HttpStatus> getRouteList(@PathVariable Long id){
        transitService.updateRouteSeen(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PostMapping("/api/route/link")
//    public ResponseEntity<String> linkRoute(@RequestParam Long routeId1, @RequestParam int infoId1,@RequestParam Long routeId2,@RequestParam int infoId2){
//        transitService.linkRoute(routeId1, infoId1, routeId2, infoId2);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}


