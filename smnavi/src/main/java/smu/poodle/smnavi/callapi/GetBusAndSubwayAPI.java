package smu.poodle.smnavi.callapi;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static smu.poodle.smnavi.callapi.Const.SERVICE_KEY;

@Component
@RequiredArgsConstructor
public class GetBusAndSubwayAPI {
    private final String HOST_URL = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBusNSub";

    private final BusRouteListAPI busRouteListAPI;

//    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        GetBusAndSubwayAPI getBusAndSubwayAPI = new GetBusAndSubwayAPI(new BusRouteListAPI());
//        List<PathInfo> pathInfo = getBusAndSubwayAPI.getTransitRoute(new GpsPoint("126.911128", "37.6062954"));
//
//        System.out.println(pathInfo.toString());
//
//    }

    /**
     * 출발지의 좌표값을 이용해 상명대학교까지의 대중교통을 이용한 경로 반환
     * @param startPoint 출발지 좌표
     * @return 이용 교통수단 정보 및 경로 좌표 데이터 리스트
     */
    public List<PathInfo> getTransitRoute(GpsPoint startPoint) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(HOST_URL);
            urlBuilder.append("?");
            urlBuilder.append("serviceKey=" + SERVICE_KEY);
            urlBuilder.append("&startX=" + startPoint.getGpsX());
            urlBuilder.append("&startY=" + startPoint.getGpsY());
            urlBuilder.append("&endX=" + Const.SMU_X);
            urlBuilder.append("&endY=" + Const.SMU_Y);

            try {
                Document doc = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(urlBuilder.toString());

                NodeList itemList = doc.getElementsByTagName("itemList");
                List<PathInfo> pathInfoList = new ArrayList<>();
                List<GpsPoint> gpsPointList = new ArrayList<>();


                for(int temp = 0; temp < itemList.getLength() && temp < 5; temp++) {
                    Node iNode = itemList.item(temp);

                    Element iElement = (Element) iNode;

                    NodeList pathList = iElement.getElementsByTagName("pathList");

                    List<TransitInfo> transitInfoList = new ArrayList<>();

                    for (int i = 0; i < pathList.getLength(); i++) {
                        Node pNode = pathList.item(i);
                        Element pElement = (Element) pNode;

                        String busNum = ApiUtilMethod.getTagValue("routeNm", pElement);
                        String from = ApiUtilMethod.getTagValue("fname", pElement);
                        String to = ApiUtilMethod.getTagValue("tname", pElement);

                        if (pElement.getElementsByTagName("routeId").getLength() != 0) {
                            String busId = ApiUtilMethod.getTagValue("routeId", pElement);
                            transitInfoList.add(new TransitInfo(TRANSIT.BUS, busNum, from, to));
                            busRouteListAPI.getRouteList(gpsPointList, busId, from, to);
                        } else {
                            //지하철 경로일 경우 코드가 들어올 부분
                            transitInfoList.add(new TransitInfo(TRANSIT.SUBWAY, busNum, from, to));
                        }
                    }
                    int time = Integer.parseInt(ApiUtilMethod.getTagValue("time", iElement));
                    PathInfo pathInfo = new PathInfo(transitInfoList, gpsPointList, time);
                    pathInfoList.add(pathInfo);
                }
                return pathInfoList;

            }
            catch (Exception e){
                return null;
            }
    }
}
