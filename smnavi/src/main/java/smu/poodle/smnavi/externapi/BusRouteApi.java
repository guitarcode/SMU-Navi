package smu.poodle.smnavi.externapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smu.poodle.smnavi.errorcode.CommonErrorCode;
import smu.poodle.smnavi.exception.RestApiException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

@Component
@Slf4j
public class BusRouteApi {
    private final String HOST_URL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute";

    /**
     * 버스 경유 정류소 목록을 불러 좌표를 gpsList 에 추가
     * @param gpsPointList gps 정보가 담긴 List
     * @param busId 공공데이터 버스 고유 id
     * @param from 버스 출발 정류소 이름
     * @param to 버스 도착 정류소 이름
     */
    public void getRouteList(List<String> stationList, List<GpsPoint> gpsPointList, String busId, String from, String to){
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(HOST_URL);
        urlBuilder.append("?serviceKey=" + Const.SERVICE_KEY);
        urlBuilder.append("&busRouteId=" + busId);

        try {
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(urlBuilder.toString());


            NodeList itemList = doc.getElementsByTagName("itemList");

            int temp;
            //
            boolean direction = true;

            for (temp = 0; temp < itemList.getLength(); temp++) {

                Node iNode = itemList.item(temp);

                Element iElement = (Element) iNode;

                String stationName = ApiUtilMethod.getTagValue("stationNm", iElement);

                if(stationName.equals(from)){
                    if(direction) {
                        String gpsX = ApiUtilMethod.getTagValue("gpsX", iElement);
                        String gpsY = ApiUtilMethod.getTagValue("gpsY", iElement);
                        gpsPointList.add(new GpsPoint(gpsX, gpsY));
                        break;
                    }
                    else {
                        direction = true;
                    }
                }
                else if(stationName.equals(to)){
                    direction = false;
                }
            }

            while(temp < itemList.getLength()){
                Node iNode = itemList.item(temp);
                Element iElement = (Element) iNode;
                String stationName = ApiUtilMethod.getTagValue("stationNm", iElement);

                if(stationName.equals(to))
                    break;

                String gpsX = ApiUtilMethod.getTagValue("gpsX", iElement);
                String gpsY = ApiUtilMethod.getTagValue("gpsY", iElement);

                stationList.add(stationName);
                gpsPointList.add(new GpsPoint(gpsX, gpsY));
                temp++;
            }
        }
        catch (Exception e){
            log.error("버스 루트 검색 중 오류 발생");
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
