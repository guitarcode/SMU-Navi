package smu.poodle.smnavi.externapi;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import smu.poodle.smnavi.errorcode.ErrorCode;
import smu.poodle.smnavi.exception.RestApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiUtilMethod {
    public static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

        String result = nlList.item(0).getTextContent();

        return result;
    }


    public static JSONObject urlBuildWithJson(final String hostUrl, ErrorCode errorCode, ApiKeyValue ...params){
        StringBuilder sb = new StringBuilder();
        sb.append(hostUrl + "?");

        int i;
        for (i = 0; i < params.length - 1; i++) {
            sb.append(params[i].getKey() + "=" + params[i].getValue() + "&");
        }

        sb.append(params[i].getKey() + "=" + params[i].getValue());

        try {
            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                throw new RestApiException(errorCode);
            }
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                jsonBuilder.append(line);
            }
            //중간에서 예외터져서 여기서 못닫으면??
            rd.close();
            conn.disconnect();
            return new JSONObject(jsonBuilder.toString());
        }
        catch (Exception e){
            throw new RestApiException(errorCode);
        }
    }
}
