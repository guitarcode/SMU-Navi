package smu.poodle.smnavi.map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smu.poodle.smnavi.map.dto.RouteDto;
import smu.poodle.smnavi.map.dto.TransitPathDto;
import smu.poodle.smnavi.map.response.BaseResponse;
import smu.poodle.smnavi.map.response.TransitResponse;
import smu.poodle.smnavi.map.service.TransitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransitController {

    private final TransitService transitService;

    /**
     * startStationId 를 통해 경로를 불러오는 api
     * @param startStationId
     * @return
     */
    @GetMapping("/api/route/{startStationId}")
    public ResponseEntity<BaseResponse> getRoute(@PathVariable String startStationId){

        List<TransitPathDto> transitRoute = transitService.getTransitRoute(startStationId);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .pathInfoCnt(transitRoute.size())
                .build();


        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }

    /**
     * 정해진 시작 지점의 아이디와 정류장이름을 모두 반환하는 API
     * @return 루트 DTO 리스트
     */

    @GetMapping("/api/route")
    public ResponseEntity<List<RouteDto>> getRouteList(){
        List<RouteDto> routeList = transitService.getRouteList();

        return new ResponseEntity<>(routeList, HttpStatus.OK);
    }
}


