package smu.poodle.smnavi.callapi;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ApiUtilMethod {
    public static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

        String result = nlList.item(0).getTextContent();

        return result;
    }
}
