package smu.poodle.smnavi.externapi.accident;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.externapi.ApiKeyValue;
import smu.poodle.smnavi.externapi.ApiUtilMethod;
import smu.poodle.smnavi.externapi.GpsPoint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Component
public class AccidentApi {
    public void getAccidentInfo() throws ParserConfigurationException, IOException, SAXException {
        String url = "http://openapi.seoul.go.kr:8088/49754d766b63686f3533714966414c/xml/AccInfo/1/5/";

        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
        Document xmlContent = dBuilder.parse(url);

        xmlContent.getDocumentElement().normalize();

        NodeList nList = xmlContent.getElementsByTagName("row");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            Element eElement = (Element) nNode;

            String tmX = ApiUtilMethod.getTagValue("grs80tm_x", eElement);
            String tmY = ApiUtilMethod.getTagValue("grs80tm_y", eElement);
            GpsPoint gpsPoint = convertGps(tmX, tmY);
            System.out.println(gpsPoint.getGpsX() + ", " +gpsPoint.getGpsY());
        }

    }

    public GpsPoint convertGps(String tmX, String tmY){
        JSONObject jsonContent = ApiUtilMethod
                .urlBuildWithJson("https://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.json"
                        ,ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS
                        ,new ApiKeyValue("accessToken", "a658a7ca-e889-46a6-b5f1-26e7d17a7f14")
                        ,new ApiKeyValue("src", "5181")
                        ,new ApiKeyValue("dst", "4326")
                        ,new ApiKeyValue("posX", tmX)
                        ,new ApiKeyValue("posY", tmY));

        JSONObject posInfo = jsonContent.getJSONObject("result");
        String posX = posInfo.getBigDecimal("posX").toString();
        String posY = posInfo.getBigDecimal("posY").toString();

        return new GpsPoint(posX,posY);
    }
}
