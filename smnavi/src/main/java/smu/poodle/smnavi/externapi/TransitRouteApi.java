package smu.poodle.smnavi.externapi;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.exception.RestApiException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

import static smu.poodle.smnavi.externapi.Const.SERVICE_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransitRouteApi {
    private final String HOST_URL = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBusNSub";

    private final BusRouteApi busRouteListAPI;

    /**
     * 출발지의 좌표값을 이용해 상명대학교까지의 대중교통을 이용한 경로 반환
     * @param startX 출발지 X 좌표
     * @param startY 출발지 Y 좌표
     * @return 이용 교통수단 정보 및 경로 좌표 데이터 리스트
     */
    public List<TransitPathInfoDto> getTransitRoute(String startX, String startY) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(HOST_URL);
            urlBuilder.append("?");
            urlBuilder.append("serviceKey=" + SERVICE_KEY);
            urlBuilder.append("&startX=" + startX);
            urlBuilder.append("&startY=" + startY);
            urlBuilder.append("&endX=" + Const.SMU_X);
            urlBuilder.append("&endY=" + Const.SMU_Y);

            try {
                Document doc = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(urlBuilder.toString());

                NodeList itemList = doc.getElementsByTagName("itemList");

                if(itemList.getLength() == 0){
                    throw new RestApiException(ExternApiErrorCode.NO_PATH_FOUND);
                }

                List<TransitPathInfoDto> pathInfoList = new ArrayList<>();
                List<GpsPoint> gpsPointList = new ArrayList<>();


                for(int temp = 0; temp < itemList.getLength() && temp < 5; temp++) {
                    Node iNode = itemList.item(temp);

                    Element iElement = (Element) iNode;

                    NodeList pathList = iElement.getElementsByTagName("pathList");

                    List<TransitPathInfoDto.TransitInfo> transitInfoList = new ArrayList<>();

                    for (int i = 0; i < pathList.getLength(); i++) {
                        Node pNode = pathList.item(i);
                        Element pElement = (Element) pNode;

                        String busName = ApiUtilMethod.getTagValue("routeNm", pElement);
                        String from = ApiUtilMethod.getTagValue("fname", pElement);
                        String to = ApiUtilMethod.getTagValue("tname", pElement);

                        transitInfoList.add(TransitPathInfoDto.TransitInfo.builder()
                                .type(TRANSIT.BUS)
                                .name(busName)
                                .from(from)
                                .to(to)
                                .build());

                        if (pElement.getElementsByTagName("routeId").getLength() != 0) {
                            String busId = ApiUtilMethod.getTagValue("routeId", pElement);
                            busRouteListAPI.getRouteList(gpsPointList, busId, from, to);
                        } else {
                            transitInfoList.add(new TransitPathInfoDto.TransitInfo(TRANSIT.SUBWAY, busName, from, to));
                        }
                    }
                    int time = Integer.parseInt(ApiUtilMethod.getTagValue("time", iElement));
                    TransitPathInfoDto pathInfo = new TransitPathInfoDto(transitInfoList, gpsPointList, time);
                    pathInfoList.add(pathInfo);
                }
                return pathInfoList;

            }
            catch(Exception e){
                //체크 예외를 catch 해서 내가 커스텀한 언체크 예외로 다시 throw 해도 되는건가..?
                log.error(e.getMessage());
                throw new RestApiException(ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS);
            }

    }
}
