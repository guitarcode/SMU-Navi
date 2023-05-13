package smu.poodle.smnavi.map.externapi.businfo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smu.poodle.smnavi.map.domain.BusRouteInfo;
import smu.poodle.smnavi.map.domain.Route;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;
import smu.poodle.smnavi.map.externapi.ApiConstantValue;
import smu.poodle.smnavi.map.externapi.ApiKeyValue;
import smu.poodle.smnavi.map.externapi.ApiUtilMethod;
import smu.poodle.smnavi.map.repository.TransitRepository;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BusInfoApi {
    private final ApiConstantValue apiConstantValue;
    private final TransitRepository transitRepository;

    private final String HOST_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";

    public int routeTime(String startX, String startY, int idx){
        JSONObject transitJson = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", apiConstantValue.getOdsayApiKey()),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", apiConstantValue.getSMU_X()),
                new ApiKeyValue("EY", apiConstantValue.getSMU_Y()));

        JSONArray pathList = transitJson.getJSONObject("result").getJSONArray("path");

        JSONObject path = pathList.getJSONObject(idx);
        JSONObject pathInfo = path.getJSONObject("info");
        int time = pathInfo.getInt("totalTime");

        return time;
    }

    public Document getBusPosInfo(String busId, int startOrd, int endOrd) {
        String url = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRouteSt?ServiceKey=bFcIfbKjGI8rVFG9xZouBt%2B3s0kITpf0u6Loz8ekrvseXj%2Bye16tUmvGrBgLdK5zbVA3cAanmNPa%2F1o%2B2n2feQ%3D%3D&busRouteId=" + busId
                +"&startOrd=" + startOrd
                +"&endOrd=" + endOrd;

        return ApiUtilMethod.callXmlApi(url);
    }

    public List<BusRouteInfo> parsingBusPosInfo(Document xmlContent, int time, Route route){

        List<BusRouteInfo> busRouteInfoList = new ArrayList<>();

        NodeList itemList = xmlContent.getElementsByTagName("itemList");

        for (int i = 0; i < itemList.getLength(); i++) {
            Node node = itemList.item(i);

            Element element = (Element) node;


            int congetion = Integer.parseInt(ApiUtilMethod.getTagValue("congetion", element));
            boolean isFull = ApiUtilMethod.getTagValue("isFullFlag", element).equals("1");

            BusRouteInfo busRouteInfo = new BusRouteInfo();
            busRouteInfo.setCongestion(congetion);
            busRouteInfo.setFull(isFull);
            busRouteInfo.setRoute(route);
            busRouteInfo.setTime(time);
            busRouteInfoList.add(busRouteInfo);
        }


        for (BusRouteInfo busRouteInfo : busRouteInfoList) {
            busRouteInfo.setRoute(route);
            busRouteInfo.setTime(time);

            transitRepository.saveBusRouteInfo(busRouteInfo);
        }

        if(busRouteInfoList.isEmpty()){
            BusRouteInfo busRouteInfo = new BusRouteInfo();
            busRouteInfo.setTime(time);
            busRouteInfo.setRoute(route);
            transitRepository.saveBusRouteInfo(busRouteInfo);
        }

        return busRouteInfoList;
    }
}
