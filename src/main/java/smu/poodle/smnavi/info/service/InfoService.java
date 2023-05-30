package smu.poodle.smnavi.info.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.exceptiony.DuplicateInfoException;
import smu.poodle.smnavi.exceptiony.DuplicateNoticeException;
import smu.poodle.smnavi.exceptiony.InfoNotFoundException;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.info.domain.InfoEntity;
import smu.poodle.smnavi.info.domain.Location;
import smu.poodle.smnavi.info.dto.InfoDto;
import smu.poodle.smnavi.info.dto.LocationDto;
import smu.poodle.smnavi.info.repository.InfoRepository;
import smu.poodle.smnavi.map.domain.data.TransitType;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.repository.AccidentRepository;
import smu.poodle.smnavi.map.repository.BusStationRepository;
import smu.poodle.smnavi.map.repository.SubwayStationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final InfoRepository infoRepository;
    private final BusStationRepository busStationRepository;
    private final SubwayStationRepository subwayStationRepository;
    private final AccidentRepository accidentRepository;


    public void addInfo(InfoDto infoDto) {
        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1); //1분 전의 시간
        int noticeCount = infoRepository.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                infoDto.getTitle(),
                infoDto.getContent(),
                checkDate
        );
        if (noticeCount > 0) {
            throw new DuplicateNoticeException("1분 이내에 동일한 내용의 제보 글이 등록되었습니다."); //제목이나 내용이 달라야함. id만 다르면 안됨
        }

        InfoEntity infoEntity = infoDto.ToEntity();
        if (infoEntity.getTransitType() == TransitType.BUS) {
            Waypoint waypoint = busStationRepository.findAllByLocalStationId(infoDto.getStationId()).get(0);
            infoEntity.setWaypoint(waypoint);
        }
        else if (infoEntity.getTransitType() == TransitType.SUBWAY) {
            Waypoint waypoint = subwayStationRepository.findAllByStationId(Integer.parseInt(infoDto.getStationId())).get(0);
            infoEntity.setWaypoint(waypoint);
        }

        createAccident(infoEntity);

        infoRepository.save(infoEntity);
    }

    private void createAccident(InfoEntity infoEntity){
        Accident accident = Accident.builder()
                .kind(infoEntity.getKind())
                .waypoint(infoEntity.getWaypoint())
                .build();

        accidentRepository.save(accident);
    }

    public List<InfoDto> listAllinfo(String keyword) {
        List<InfoEntity> all = null;
        if (keyword == null || keyword.isEmpty()) {
            all = infoRepository.findAll();
        } else {
            all = infoRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        }
        List<InfoDto> DtoList = new ArrayList<>();

        for (InfoEntity infoEntity : all) {
            InfoDto infoDto = InfoDto.builder()
                    .id(infoEntity.getId())
                    .title(infoEntity.getTitle())
                    .content(infoEntity.getContent())
                    .regDate(infoEntity.getRegDate())
                    .updateDate(infoEntity.getUpdateDate())
                    .build();
            DtoList.add(infoDto);
        }


        return DtoList;
    }

    public Optional<InfoEntity> updateInfo(Long id, InfoDto infoDto) {
        LocalDateTime updateTime = LocalDateTime.now().minusMinutes(1);
        int infoCount = infoRepository.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                infoDto.getTitle(),
                infoDto.getContent(),
                updateTime
        );
        if (infoCount > 0) {
            throw new DuplicateInfoException("수정된 내용이 없습니다.");
        }
        InfoEntity infoEntity = infoRepository.findById(id)
                .orElseThrow(() -> new InfoNotFoundException("해당 제보 글을 찾을 수 없습니다."));
        infoEntity.setTitle(infoDto.getTitle());
        infoEntity.setContent(infoDto.getContent());
        infoRepository.save(infoEntity);
        return Optional.of(infoEntity);
    }

    public void increaseViews(Long id) {
        InfoEntity infoEntity = infoRepository.findById(id).orElseThrow(() -> new InfoNotFoundException("해당 제보 글을 찾을 수 없습니다."));
        infoEntity.increaseViews();
        infoRepository.save(infoEntity);
    }

    public Optional<InfoDto> getInfoById(Long id) {
        Optional<InfoEntity> infoEntity = infoRepository.findById(id);
        Optional<InfoDto> infoDto = Optional.ofNullable(InfoDto.builder()
                .id(infoEntity.get().getId())
                .title(infoEntity.get().getTitle())
                .content(infoEntity.get().getContent())
                .regDate(infoEntity.get().getRegDate())
                .updateDate(infoEntity.get().getUpdateDate())
                .increaseCount(infoEntity.get().getIncreaseCount())
                .build());
        return infoDto;
    }

    public Long deleteInfoId(Long id) {
        Optional<InfoEntity> infoEntity = infoRepository.findById(id);
        if (!infoEntity.isPresent()) {
            throw new DuplicateInfoException("삭제할 내용이 없습니다.");
        } else {
            infoRepository.delete(infoEntity.get());
            return infoEntity.get().getId();
        }
    }

    public List<LocationDto> getBusLocationList() {
        List<Location> busTransitType = Location.getByTransitType(TransitType.BUS);
        List<Location> subTransitType = Location.getByTransitType(TransitType.SUBWAY);
//      List<LocationDto> locations = (List<LocationDto>) LocationDto.from(busTransitType,);
        List<LocationDto> locations = new ArrayList<>();
        locations.add(LocationDto.from("버스", busTransitType));
        locations.add(LocationDto.from("지하철", subTransitType));
        return locations;
    }
}
