package smu.poodle.smnavi.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.mapping.SubPathAndEdge;
import smu.poodle.smnavi.map.domain.path.Edge;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.repository.SubPathAndEdgeRepository;
import smu.poodle.smnavi.map.repository.SubPathRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubPathService {

    private final SubPathRepository subPathRepository;

    private final SubPathAndEdgeRepository subPathAndEdgeRepository;

    public void saveWithEdgeMapping(SubPath subPath, List<Edge> edges){
        //todo: 겹치는 서브 패스가 있는지 확인해야함
        subPathRepository.save(subPath);

        for (Edge edge : edges) {
            subPathAndEdgeRepository.save(
                    SubPathAndEdge.builder()
                            .subPath(subPath)
                            .edge(edge)
                            .build()
            );
        }
    }

}
