//package smu.poodle.smnavi.map.externapi.accident;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import smu.poodle.smnavi.map.domain.DetailPosition;
//import smu.poodle.smnavi.map.domain.Edge;
//import smu.poodle.smnavi.map.domain.Route;
//import smu.poodle.smnavi.map.domain.RouteInfo;
//import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
//import smu.poodle.smnavi.map.externapi.ApiKeyValue;
//import smu.poodle.smnavi.map.externapi.ApiUtilMethod;
//import smu.poodle.smnavi.map.externapi.GpsPoint;
//import smu.poodle.smnavi.map.repository.TransitRepository;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@Transactional
//public class AccidentApi {
//
//    private final TransitRepository transitRepository;
//
//
//    public void getAccidentInfo() throws ParserConfigurationException, IOException, SAXException {
//        String url = "http://openapi.seoul.go.kr:8088/49754d766b63686f3533714966414c/xml/AccInfo/1/5/";
//
//        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
//        Document xmlContent = dBuilder.parse(url);
//
//        xmlContent.getDocumentElement().normalize();
//
//        NodeList nList = xmlContent.getElementsByTagName("row");
//
//        for (int i = 0; i < nList.getLength(); i++) {
//            Node nNode = nList.item(i);
//
//            Element eElement = (Element) nNode;
//
//            String tmX = ApiUtilMethod.getTagValue("grs80tm_x", eElement);
//            String tmY = ApiUtilMethod.getTagValue("grs80tm_y", eElement);
//
//            GpsPoint gpsPoint = convertGps(tmX, tmY);
//            GpsPoint editedGpsPoint = new GpsPoint(gpsPoint.getGpsX().substring(0,7), gpsPoint.getGpsY().substring(0,6));
//
//            String accidentInfo = ApiUtilMethod.getTagValue("acc_info", eElement);
//            updateAccidentInfo(gpsPoint, editedGpsPoint, accidentInfo);
//        }
//    }
//
//    public void updateAccidentInfo(GpsPoint gpsPoint, GpsPoint editedGpsPoint, String info){
//        List<DetailPosition> similarPoint = transitRepository.findSimilarPoint(editedGpsPoint);
//        ArrayList<DetailPosition> detailPositions = new ArrayList<>();
//        System.out.println(similarPoint);
//
//        for (DetailPosition detailPosition : similarPoint) {
//            GpsPoint gpsPoint1 = new GpsPoint(detailPosition.getX(), detailPosition.getY());
//
//            System.out.println(gpsPoint1);
//            double distance = ApiUtilMethod.calculateDistance(gpsPoint1, gpsPoint);
//            System.out.println(distance);
//            if(distance < 200D)
//                detailPositions.add(detailPosition);
//        }
//
//        List<Edge> edgeList = new ArrayList<>();
//        for (DetailPosition detailPosition : detailPositions) {
//            Edge edge = detailPosition.getEdge();
//            edgeList.add(edge);
//            log.debug(edge.getSrc().getStationName());
//            System.out.println(edge.getSrc().getStationName() + "to" + edge.getDst().getStationName());
//        }
//        edgeList = edgeList.stream().distinct().collect(Collectors.toList());
//
//        for (Edge edge : edgeList) {
//            List<RouteInfo> routeInfoList = edge.getRouteInfoList();
//            for (RouteInfo routeInfo : routeInfoList) {
//                System.out.println("루트아이디 : "+ routeInfo.getRoute().getId());
//                Route route = routeInfo.getRoute();
//                route.updateAccident(info);
//                System.out.println(route.getId() + "사고 업데이트 완료");
//                log.debug(route.getId() + "사고 업데이트 완료");
//            }
//        }
//    }
//
//    public GpsPoint convertGps(String tmX, String tmY){
//        JSONObject jsonContent = ApiUtilMethod
//                .urlBuildWithJson("https://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.json"
//                        ,ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS
//                        ,new ApiKeyValue("accessToken", "fd37bce8-15da-4559-a15e-a4a69d4613d6")
//                        ,new ApiKeyValue("src", "5181")
//                        ,new ApiKeyValue("dst", "4326")
//                        ,new ApiKeyValue("posX", tmX)
//                        ,new ApiKeyValue("posY", tmY));
//
//        JSONObject posInfo = jsonContent.getJSONObject("result");
//        String posX = posInfo.getBigDecimal("posX").toString();
//        String posY = posInfo.getBigDecimal("posY").toString();
//
//        return new GpsPoint(posX,posY);
//    }
//
//}
