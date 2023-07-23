package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.repository.FullPathAndSubPathRepository;
import smu.poodle.smnavi.map.repository.FullPathRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FullPathService {
    private final FullPathRepository fullPathRepository;
    private final FullPathAndSubPathRepository fullPathAndSubPathRepository;

    public FullPath saveFullPathMappingSubPath(FullPath fullPath, List<SubPath> subPathList){

        List<FullPath> persistedFullPath = fullPathAndSubPathRepository.findAllBySubPath(subPathList, subPathList.size());

        if(!persistedFullPath.isEmpty()) {
            log.info("이미 존재하는 경로 정보입니다. FullPath 번호 : " + persistedFullPath.get(0));
            return persistedFullPath.get(0);
        }
        else {
            fullPathRepository.save(fullPath);

            for (SubPath subPath : subPathList) {
                fullPathAndSubPathRepository.save(FullPathAndSubPath.builder()
                        .fullPath(fullPath)
                        .subPath(subPath)
                        .build());
            }

            return fullPath;
        }
    }
}
