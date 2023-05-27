package smu.poodle.smnavi.map.service.manage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.map.domain.data.TransitType;
import smu.poodle.smnavi.map.domain.path.Edge;
import smu.poodle.smnavi.map.domain.path.FullPath;
import smu.poodle.smnavi.map.domain.path.SubPath;
import smu.poodle.smnavi.map.domain.station.Waypoint;
import smu.poodle.smnavi.map.dto.PathDto;
import smu.poodle.smnavi.map.dto.WaypointDto;
import smu.poodle.smnavi.map.odsay.RouteDetailPositionApi;
import smu.poodle.smnavi.map.repository.EdgeRepository;
import smu.poodle.smnavi.map.service.FullPathService;
import smu.poodle.smnavi.map.service.SubPathService;
import smu.poodle.smnavi.map.service.WaypointService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PathManageService {
    private final WaypointService waypointService;
    private final EdgeRepository edgeRepository;
    private final SubPathService subPathService;
    private final FullPathService fullPathService;

    private final RouteDetailPositionApi routeDetailPositionApi;

    @Transactional
    public void savePaths(PathDto.Info pathDto) {
        List<SubPath> subPaths = new ArrayList<>(pathDto.getSubPathList().size());

        for (int i = 0; i < pathDto.getSubPathList().size(); i++) {
            subPaths.add(SubPath.builder().build());
        }

        for (int i = 0; i < pathDto.getSubPathList().size(); i++) {
            PathDto.SubPathDto subPathDto = pathDto.getSubPathList().get(i);

            if (subPathDto.getTransitType() == TransitType.WALK)
                continue;

            //이동 수단이 버스나 지하철인 경우
            List<Waypoint> waypoints = new ArrayList<>();

            for (WaypointDto waypointDto : subPathDto.getStationList()) {
                Waypoint waypoint = waypointDto.toEntity();
                Waypoint persistedWaypoint = waypointService.save(waypoint);
                waypoints.add(persistedWaypoint);
            }

            List<Edge> edges = makeEdgeExceptWalk(waypoints);
            List<Edge> persistedEdges = new ArrayList<>();

            // 버스나 지하철인 경우 정류장 및 엣지 정보를 우선 저장
            for (int j = 0; j < edges.size(); j++) {
                Edge edge = edges.get(j);

                Optional<Edge> persistedEdge = edgeRepository.findFirstBySrcIdAndDstId(edge.getSrc().getId(), edge.getDst().getId());

                if (persistedEdge.isPresent()) {
                    persistedEdges.add(persistedEdge.get());
                } else {
                    persistedEdges.add(edgeRepository.save(edge));
                }

                //엣지의 디테일 루트 만들기
                String[] mapObjArr = pathDto.getMapObj().split("@");

                for (String s : mapObjArr) {
                    routeDetailPositionApi.makeDetailPositionList(
                            subPathDto,
                            s,
                            persistedEdges);
                }
            }

            SubPath subPath = SubPath.builder()
                    .sectionTime(subPathDto.getSectionTime())
                    .transitType(subPathDto.getTransitType())
                    .fromName(subPathDto.getFrom())
                    .toName(subPathDto.getTo())
                    .src(waypoints.get(0))
                    .dst(waypoints.get(waypoints.size() - 1))
                    .build();

            subPathService.saveWithEdgeMapping(subPath, persistedEdges);

            subPaths.set(i, subPath);
        }

        for (int i = 0; i < pathDto.getSubPathList().size(); i++) {
            PathDto.SubPathDto subPathDto = pathDto.getSubPathList().get(i);

            if (subPathDto.getTransitType() == TransitType.WALK) {
                List<Edge> edges = new ArrayList<>();
                // 0번째는 WALK 일 수 없도록 처리하였음
                Waypoint src, dst;
                src = subPaths.get(i - 1).getDst();

                //마지막 서브패스는 무조건 걷는 것
                if (i == pathDto.getSubPathList().size() - 1) {
                    //todo: 상명대 위치 픽스하기
                    dst = Waypoint.builder()
                            .x("126.955252")
                            .y("37.602638")
                            .build();

                    waypointService.save(dst);
                } else {
                    dst = subPaths.get(i + 1).getSrc();
                }

                Edge edge = Edge.builder()
                        .src(src)
                        .dst(dst)
                        .detailExist(false)
                        .build();

                //todo: edge 서비스로 옮기기
                edgeRepository.save(edge);

                edges.add(edge);

                SubPath subPath = SubPath.builder()
                        .src(src)
                        .dst(dst)
                        .fromName(edge.getSrc().getPointName())
                        .toName(edge.getDst().getPointName())
                        .sectionTime(subPathDto.getSectionTime())
                        .transitType(TransitType.WALK)
                        .build();

                subPaths.set(i, subPath);

                subPathService.saveWithEdgeMapping(subPath, edges);
            }
        }

        FullPath fullPath = FullPath.builder()
                .isSeen(true)
                .startWaypoint(subPaths.get(0).getSrc())
                .totalTime(pathDto.getTime())
                .build();

        fullPathService.saveFullPathMappingSubPath(fullPath, subPaths);
    }

    public List<Edge> makeEdgeExceptWalk(List<Waypoint> waypoints) {
        List<Edge> edges = new ArrayList<>();
        Waypoint src = null;

        for (Waypoint waypoint : waypoints) {
            if (src == null) {
                src = waypoint;
            } else {
                edges.add(Edge.builder()
                        .src(src)
                        .dst(waypoint)
                        .detailExist(false)
                        .build());

                src = waypoint;
            }
        }

        return edges;
    }
}
