package smu.poodle.smnavi.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.exceptiony.DuplicateInfoException;
import smu.poodle.smnavi.exceptiony.DuplicateNoticeException;
import smu.poodle.smnavi.exceptiony.InfoNotFoundException;
import smu.poodle.smnavi.info.domain.InfoEntity;
import smu.poodle.smnavi.info.dto.InfoDto;
import smu.poodle.smnavi.info.repository.InfoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InfoService {
    @Autowired
    private InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository){
        this.infoRepository = infoRepository;
    }
    public void addInfo(InfoDto infoDto){
        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1); //1분 전의 시간
        int noticeCount = infoRepository.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                infoDto.getTitle(),
                infoDto.getContent(),
                checkDate
        );
        if (noticeCount > 0) {
            throw new DuplicateNoticeException("1분 이내에 동일한 내용의 제보 글이 등록되었습니다."); //제목이나 내용이 달라야함. id만 다르면 안됨
        }
        infoRepository.save(infoDto.ToEntity());
    }
    public List<InfoDto> listAllinfo(String keyword){
        List<InfoEntity> all = null;
        if(keyword==null||keyword.isEmpty()){
            all = infoRepository.findAll();
        }else{
            all = infoRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        }
        List<InfoDto>DtoList = new ArrayList<>();
        for(InfoEntity infoEntity : all){

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
    public Optional<InfoEntity> updateInfo(Long id, InfoDto infoDto){
        LocalDateTime updateTime = LocalDateTime.now().minusMinutes(1);
        int infoCount = infoRepository.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                infoDto.getTitle(),
                infoDto.getContent(),
                updateTime
        );
        if(infoCount>0){
            throw new DuplicateInfoException("수정된 내용이 없습니다.");
        }
        InfoEntity infoEntity = infoRepository.findById(id)
                .orElseThrow(()->new InfoNotFoundException("해당 제보 글을 찾을 수 없습니다."));
        infoEntity.setTitle(infoDto.getTitle());
        infoEntity.setContent(infoDto.getContent());
        infoRepository.save(infoEntity);
        return Optional.of(infoEntity);
    }
    public void increaseViews(Long id){
        InfoEntity infoEntity = infoRepository.findById(id).orElseThrow(()->new InfoNotFoundException("해당 제보 글을 찾을 수 없습니다."));
        infoEntity.increaseViews();
        infoRepository.save(infoEntity);
    }
    public Optional<InfoDto>getInfoById(Long id){
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
    public Long deleteInfoId(Long id){
        Optional<InfoEntity> infoEntity = infoRepository.findById(id);
        if(!infoEntity.isPresent()){
            throw new DuplicateInfoException("삭제할 내용이 없습니다.");
        }else{
            infoRepository.delete(infoEntity.get());
            return infoEntity.get().getId();
        }
    }

}
