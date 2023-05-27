package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.mapping.FullPathAndSubPath;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.repository.FullPathAndSubPathRepository;
import smu.poodle.smnavi.map.repository.FullPathRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FullPathService {
    private final FullPathRepository fullPathRepository;
    private final FullPathAndSubPathRepository fullPathAndSubPathRepository;

    public void saveFullPathMappingSubPath(FullPath fullPath, List<SubPath> subPaths){
        fullPathRepository.save(fullPath);

        for (SubPath subPath : subPaths) {
            fullPathAndSubPathRepository.save(FullPathAndSubPath.builder()
                    .fullPath(fullPath)
                    .subPath(subPath)
                    .build());
        }
    }
}
