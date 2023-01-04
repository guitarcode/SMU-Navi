package smu.poodle.smnavi.externapi;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import smu.poodle.smnavi.domain.SubwayStationInfo;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.exception.RestApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import smu.poodle.smnavi.service.TransitService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubwayRouteApi {
    private final String HOST_URL = "http://apis.data.go.kr/B553766/smt-path/path";
    private final TransitService transitService;

    public void getRouteList(List<String> stationList, List<GpsPoint> gpsPointList, String lineName, String from, String to) {
        StringBuilder urlBuilder = new StringBuilder(HOST_URL); /*URL*/
        urlBuilder.append("?serviceKey=" + Const.SERVICE_KEY); /*Service Key*/
        urlBuilder.append("&pageNo=1"); /*페이지번호*/
        urlBuilder.append("&numOfRows=30"); /*한 페이지 결과 수*/
        urlBuilder.append("&dept_station_code=" + from); /*출발역 코드*/
        urlBuilder.append("&dest_station_code=" + to); /*도착역 코드*/
        urlBuilder.append("&week=DAY"); /*주중(DAY)/토요일(SAT)/공휴일(HOL)*/
//        urlBuilder.append("&search_type=" + URLEncoder.encode("FASTEST", "UTF-8")); /*검색방법 (FASTEST, MINTRF)*/
//        urlBuilder.append("&" + URLEncoder.encode("first_last", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*첫차혹은막차 (첫차: 1 / 막차: 2)*/
//        urlBuilder.append("&" + URLEncoder.encode("dept_time", "UTF-8") + "=" + URLEncoder.encode("120001", "UTF-8")); /*출발시간(HHmmss)*/
//        urlBuilder.append("&" + URLEncoder.encode("train_seq", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*출발시간 기준 주변도시철도 예) -1 : 이전도시철도 1 : 다음도시철도*/
        try {
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                log.error(conn.getErrorStream().toString());
                throw new RestApiException(ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS);
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject allJson = new JSONObject(sb.toString());
            JSONArray routeJson = allJson.getJSONArray("route");

            Map<Integer, SubwayStationInfo> subwayStationInfoMap = transitService.stationInfoMap(lineName);

            for (int i = 0; i < routeJson.length(); i++) {
                JSONObject obj = routeJson.getJSONObject(i);
                String stationNum = obj.getString("station_cd");
                SubwayStationInfo subwayStationInfo = subwayStationInfoMap.get(Integer.parseInt(stationNum));
                GpsPoint gpsPoint = new GpsPoint(subwayStationInfo.getGpsX(),subwayStationInfo.getGpsY());
                gpsPointList.add(gpsPoint);
                stationList.add(obj.getString("station_nm"));
            }
        }
        catch (Exception e){
            log.error("못가져옴");
        }
    }
}
