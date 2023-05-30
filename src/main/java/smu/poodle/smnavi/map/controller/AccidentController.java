package smu.poodle.smnavi.map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.dto.AccidentDto;
import smu.poodle.smnavi.map.response.AccidentResponse;
import smu.poodle.smnavi.map.response.BaseResponse;
import smu.poodle.smnavi.map.service.AccidentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;

    @GetMapping("/api/accidents")
    public ResponseEntity<BaseResponse> getAllAccidents() {

        return new ResponseEntity(
                AccidentResponse.builder()
                        .message("성공")
                        .data(accidentService.getAllAccident())
                        .build(),
                HttpStatus.OK
        );
    }

}
