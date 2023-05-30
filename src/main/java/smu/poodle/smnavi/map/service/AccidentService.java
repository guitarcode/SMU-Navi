package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.dto.AccidentDto;
import smu.poodle.smnavi.map.repository.AccidentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccidentService {
    private final AccidentRepository accidentRepository;

    public List<AccidentDto.Info> getAllAccident(){
        //레파지토리에서 모든 사고 정보를 꺼내옴
        //DTO로 변환을 해줨
        //반환
        return accidentRepository.findAll().stream().map(AccidentDto.Info::of).toList();
    }
}
