package smu.poodle.smnavi.externapi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smu.poodle.smnavi.domain.Edge;
import smu.poodle.smnavi.domain.Station;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransitRouteApi {

    private final ApiConstantValue apiConstantValue;
    private final String HOST_URL = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBusNSub";

    @Autowired
    public TransitRouteApi(ApiConstantValue apiConstantValue) {
        this.apiConstantValue = apiConstantValue;
    }


    public List<TransitPathDto> getTransitRoute(String startX, String startY) {
        JSONObject transitJson = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", apiConstantValue.getOdsayApiKey()),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", apiConstantValue.getSMU_X()),
                new ApiKeyValue("EY", apiConstantValue.getSMU_Y()));


//        return urlString;
        return null;
    }

    private List<TransitPathDto> makePathDtoList(JSONObject transitJson){
        List<TransitPathDto> transitPathDtoList = new ArrayList<>();

        JSONArray pathList = transitJson.getJSONArray("path");
        for (int i = 0; i < pathList.length(); i++) {
            JSONObject path = pathList.getJSONObject(i);
            int time = path.getInt("totalTime");
            List<TransitSubPathDto> transitSubPathDtoList = makeSubPathDtoList(path);

            transitPathDtoList.add(TransitPathDto.builder()
                    .subPathList(transitSubPathDtoList)
                    .time(time)
                    .build());
        }
        return transitPathDtoList;
    }

    private List<TransitSubPathDto> makeSubPathDtoList(JSONObject path){
        List<TransitSubPathDto> transitSubPathDtoList = new ArrayList<>();
        StationDto preStationDto = null;

        JSONArray subPathList = path.getJSONArray("subPath");

        for (int i = 0; i < subPathList.length(); i++) {
            JSONObject subPath = subPathList.getJSONObject(i);

            int trafficType = subPath.getInt("trafficType");
            TransitType type = TransitType.of(trafficType);
            int sectionTime = subPath.getInt("sectionTime");

            if(!type.equals(TransitType.WALK)){
                String from = subPath.getString("startName");
                String to = subPath.getString("endName");

                JSONObject lane = subPath.getJSONArray("lane").getJSONObject(0);
                String laneName;
                if(type == TransitType.BUS){
                    laneName = lane.getString("BusNo");
                }
                else{
                    laneName = String.valueOf(lane.getInt("subwayCode"));
                }

                List<StationDto> stationDtoList = makeStationDtoList(subPath, preStationDto);
                preStationDto = stationDtoList.get(stationDtoList.size()-1);

                transitSubPathDtoList.add(TransitSubPathDto.builder()
                        .type(type)
                        .laneName(laneName)
                        .from(from)
                        .to(to)
                        .sectionTime(sectionTime)
                        .stationList(stationDtoList)
                        .build());
            }
        }
        return transitSubPathDtoList;
    }


    private List<StationDto> makeStationDtoList(JSONObject subPath, StationDto preStationDto){
        List<StationDto> stationDtoList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        JSONArray stationList = subPath.getJSONObject("passStopList").getJSONArray("stations");

        for (int i = 0; i < stationList.length(); i++) {
            JSONObject station = stationList.getJSONObject(i);
            String stationName = station.getString("stationName");
            int stationId = station.getInt("stationID");
            String x = station.getString("x");
            String y = station.getString("y");

            StationDto stationDto = StationDto.builder()
                    .stationId(stationId)
                    .name(stationName)
                    .gpsX(x)
                    .gpsY(y)
                    .build();

            stationDtoList.add(stationDto);

            if(preStationDto != null) {
                Edge edge = Edge.builder()
                        .src(preStationDto.toEntity())
                        .dst(stationDto.toEntity())
                        .time(0)
                        .build();
                edgeList.add(edge);
            }
            preStationDto = stationDto;
        }
        return stationDtoList;
    }
}
