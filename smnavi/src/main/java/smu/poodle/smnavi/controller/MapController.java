package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smu.poodle.smnavi.externapi.TransitRouteApi;
import smu.poodle.smnavi.externapi.TransitPathInfoDto;
import smu.poodle.smnavi.response.BaseResponse;
import smu.poodle.smnavi.response.TransitResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final TransitRouteApi transitRouteApi;

    @GetMapping("/api/map/transit")
    public ResponseEntity<BaseResponse> findTransit(@RequestParam String startX, @RequestParam String startY){

        List<TransitPathInfoDto> transitRoute = transitRouteApi.getTransitRoute(startX, startY);

        TransitResponse transitResponse = TransitResponse.builder()
                .message("정상적으로 경로를 불러왔습니다.")
                .pathInfoList(transitRoute)
                .build();

        return new ResponseEntity<>(transitResponse, HttpStatus.OK);
    }

}
