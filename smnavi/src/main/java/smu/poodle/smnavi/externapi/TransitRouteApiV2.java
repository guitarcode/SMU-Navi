package smu.poodle.smnavi.externapi;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import smu.poodle.smnavi.errorcode.ExternApiErrorCode;

import java.util.List;

@Component
public class TransitRouteApiV2 {

    private final String HOST_URL = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBusNSub";
    private final String SERVICE_KEY = "JAHjFEwFp+wvneWeC+e2n3gO4oB3VdZkCqr331Vky8A";

    public List<TransitPathInfoDto> getTransitRoute(String startX, String startY) {
        JSONObject urlString = ApiUtilMethod.urlBuildWithJson(HOST_URL,
                ExternApiErrorCode.UNSUPPORTED_OR_INVALID_GPS_POINTS,
                new ApiKeyValue("apiKey", SERVICE_KEY),
                new ApiKeyValue("SX", startX),
                new ApiKeyValue("SY", startY),
                new ApiKeyValue("EX", Const.SMU_X),
                new ApiKeyValue("EY", Const.SMU_Y));

//        return urlString;
        return null;
    }
}
