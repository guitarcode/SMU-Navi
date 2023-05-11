package smu.poodle.smnavi.map.util;

import smu.poodle.smnavi.map.domain.Edge;
import smu.poodle.smnavi.map.domain.RouteInfo;
import smu.poodle.smnavi.map.domain.Station;
import smu.poodle.smnavi.map.dto.DetailPositionDto;
import smu.poodle.smnavi.map.dto.StationDto;
import smu.poodle.smnavi.map.dto.TransitPathDto;
import smu.poodle.smnavi.map.dto.TransitSubPathDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertDtoUtil {
    public static TransitPathDto convertRouteInfoToDto(List<RouteInfo> routeInfoList, int time){
        boolean isFirst = true;
        List<TransitSubPathDto> subPathDto = new ArrayList<>();
        List<StationDto> stationList = new ArrayList<>();
        List<DetailPositionDto> gpsDetail = new ArrayList<>();
        TransitSubPathDto transitSubPathDto = null;
        StationDto stationDto = null;
        String lastLaneName = "";

        for (int i = 0; i < routeInfoList.size(); i++) {
            RouteInfo routeInfo = routeInfoList.get(i);
            Edge edge = routeInfo.getEdge();
            Station src = routeInfo.getEdge().getSrc();
            String curLaneName = src.getBusName();

            if(!lastLaneName.equals(curLaneName) & !isFirst){
                transitSubPathDto.setStationList(stationList);
                transitSubPathDto.setGpsDetail(gpsDetail);
                transitSubPathDto.setTo(stationDto.getStationName());
                subPathDto.add(transitSubPathDto);

                stationList = new ArrayList<>();
                gpsDetail = new ArrayList<>();

                isFirst = true;
            }
            if(isFirst) {
                lastLaneName = src.getBusName();
                transitSubPathDto = TransitSubPathDto.builder()
                        .type(src.getType())
                        .from(src.getStationName())
                        .laneName(lastLaneName)
                        .build();
                isFirst = false;
            }

            stationDto = StationDto.builder()
                    .localStationId(src.getLocalStationId())
                    .busName(src.getBusName())
                    .stationName(src.getStationName())
                    .gpsX(src.getX())
                    .gpsY(src.getY())
                    .build();

            stationList.add(stationDto);
            gpsDetail.addAll(edge.getDetailPositionList().stream().map(DetailPositionDto::new).collect(Collectors.toList()));
        }

        RouteInfo routeInfo = routeInfoList.get(routeInfoList.size() - 1);
        Station dst = routeInfo.getEdge().getDst();
        stationDto = StationDto.builder()
                .localStationId(dst.getLocalStationId())
                .stationName(dst.getStationName())
                .busName(dst.getBusName())
                .gpsX(dst.getX())
                .gpsY(dst.getY())
                .build();

        stationList.add(stationDto);
        transitSubPathDto.setStationList(stationList);
        transitSubPathDto.setGpsDetail(gpsDetail);
        transitSubPathDto.setTo(stationDto.getStationName());
        subPathDto.add(transitSubPathDto);

        return TransitPathDto.builder()
                .subPathList(subPathDto)
                .time(time)
                .subPathCnt(subPathDto.size())
                .build();
    }
}
