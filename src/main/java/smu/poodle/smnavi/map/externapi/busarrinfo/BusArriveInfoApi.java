package smu.poodle.smnavi.map.externapi.busarrinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smu.poodle.smnavi.map.domain.Accident;
import smu.poodle.smnavi.map.externapi.dto.BusArriveInfoData;
import smu.poodle.smnavi.map.externapi.util.XmlApiUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API REF 페이지
 * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15000314
 */
@RequiredArgsConstructor
@Component
public class BusArriveInfoApi {
    private final XmlApiUtil xmlApiUtil;

    private final String BUS_ROUTE_ID_7016 = "100100447";
    private final int START_OF_CAUTION_STATION_ORDER_7016 = 36; //용산e편한세상?
    private final int END_OF_CAUTION_STATION_ORDER_7016 = 50; //효자동

    private String makeUrl(String busRouteId) {

        final String API_BASE_URL = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll";

        final String SERVICE_KEY = "bFcIfbKjGI8rVFG9xZouBt%2B3s0kITpf0u6Loz8ekrvseXj%2Bye16tUmvGrBgLdK5zbVA3cAanmNPa%2F1o%2B2n2feQ%3D%3D";

        return API_BASE_URL + "?"
                + "serviceKey=" + SERVICE_KEY + "&"
                + "busRouteId=" + busRouteId;
    }

    public List<BusArriveInfoData> parseXml() {
        Document xmlContent = xmlApiUtil.getRootTag(makeUrl(BUS_ROUTE_ID_7016));
        Element msgBody = (Element) xmlContent.getElementsByTagName("msgBody").item(0);

        NodeList itemList = msgBody.getElementsByTagName("itemList");

        List<BusArriveInfoData> busArriveInfoDataList = new ArrayList<>();

        for (int i = START_OF_CAUTION_STATION_ORDER_7016; i < END_OF_CAUTION_STATION_ORDER_7016; i++) {
            Node itemNode = itemList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;

                String firstArrivalMessage = itemElement.getElementsByTagName("arrmsg1").item(0).getTextContent();
                String secondArrivalMessage = itemElement.getElementsByTagName("arrmsg2").item(0).getTextContent();
                int stationId = Integer.parseInt(itemElement.getElementsByTagName("stId").item(0).getTextContent());

                System.out.println("firstArrivalMessage = " + firstArrivalMessage);
                System.out.println(parseTimeString(firstArrivalMessage));
                System.out.println("secondArrivalMessage = " + secondArrivalMessage);
                System.out.println(parseTimeString(secondArrivalMessage) + "\n");

                boolean isStationNonStop = itemElement.getElementsByTagName("deTourAt").item(0).getTextContent().equals("11");

                busArriveInfoDataList.add(BusArriveInfoData.builder()
                        .stationId(stationId)
                        .firstArrivalSeconds(parseTimeString(firstArrivalMessage))
                        .secondArrivalSeconds(parseTimeString(secondArrivalMessage))
                        .isStationNonStop(isStationNonStop)
                        .firstArrivalStationOrder(calculateStationOrder(firstArrivalMessage, i))
                        .secondArrivalStationOrder(calculateStationOrder(secondArrivalMessage, i))
                        .stationOrder(i)
                        .build());
            }
        }

        return busArriveInfoDataList;
    }

    public boolean isArrivingSoon(String arriveMessage) {
        return arriveMessage.equals("곧 도착");
    }

    public List<Integer> extractNumbers(String arriveMessage) {
        List<Integer> numbers = new ArrayList<>();

        // 숫자 추출 정규식
        String regex = "\\d+";

        // 정규식 패턴 생성
        Pattern pattern = Pattern.compile(regex);

        // 입력 문자열과 정규식 매칭
        Matcher matcher = pattern.matcher(arriveMessage);

        // 매칭된 숫자 추출
        while (matcher.find()) {
            String numberStr = matcher.group();
            int number = Integer.parseInt(numberStr);
            numbers.add(number);
        }

        return numbers;
    }

    public int parseTimeString(String arriveMessage) {
        if (isArrivingSoon(arriveMessage))
            return 60;

        int minutes = 0, seconds = 0;

        List<Integer> extractedNumber = extractNumbers(arriveMessage);

        if (extractedNumber.size() == 2) {
            minutes = extractedNumber.get(0);
        } else if (extractedNumber.size() == 3) {
            minutes = extractedNumber.get(0);
            seconds = extractedNumber.get(1);
        }

        return (minutes * 60) + seconds;
    }

    public int calculateStationOrder(String arriveMessage, int stationOrder) {
        if (isArrivingSoon(arriveMessage))
            return stationOrder;

        int diffOrder = 0;
        List<Integer> extractedNumber = extractNumbers(arriveMessage);

        if (extractedNumber.size() == 2) {
            diffOrder = extractedNumber.get(1);
        } else if (extractedNumber.size() == 3) {
            diffOrder = extractedNumber.get(2);
        }

        return stationOrder - diffOrder;
    }

//    @Scheduled(cron = "0 0/10 7-17 * * *")
    public List<AccidentData> getTrafficIssue() {
        List<BusArriveInfoData> busArriveInfoDataList = parseXml();

        List<AccidentData> accidentDataList = new ArrayList<>();
        accidentDataList.add(isSpacingTooLarge(busArriveInfoDataList));
        accidentDataList.add(isSpacingTooNarrow(busArriveInfoDataList));
        accidentDataList.add(isNonStop(busArriveInfoDataList));

        return accidentDataList;
    }


    public AccidentData isSpacingTooLarge(List<BusArriveInfoData> busArriveInfoDataList) {
        for (BusArriveInfoData busArriveInfoData : busArriveInfoDataList) {
            int intervalSecond = busArriveInfoData.getSecondArrivalSeconds() - busArriveInfoData.getFirstArrivalSeconds();
            System.out.println(intervalSecond);
            if (intervalSecond >= 1200) {
                return AccidentData.builder()
                        .stationId(busArriveInfoData.getStationId())
                        .message("배차간격이 " + intervalSecond / 60 + "분 이상입니다.")
                        .build();
            }
        }
        return null;
    }

    public AccidentData isSpacingTooNarrow(List<BusArriveInfoData> busArriveInfoDataList) {
        List<Integer> busLocatedStationOrderList = createBusLocatedStationOrderList(busArriveInfoDataList);

        Queue<Integer> busQueue = new LinkedList<>();

        for (Integer busLocatedStationOrder : busLocatedStationOrderList) {
            busQueue.offer(busLocatedStationOrder);

            while (!busQueue.isEmpty() && busLocatedStationOrder - busQueue.peek() > 2) {
                busQueue.poll();
            }

            if (busQueue.size() >= 3) {
                for (BusArriveInfoData busArriveInfoData : busArriveInfoDataList) {
                    if(busArriveInfoData.getStationOrder() == busLocatedStationOrder) {
                        return AccidentData.builder()
                                .stationId(busArriveInfoData.getStationId())
                                .message("해당 정류장 근처에서 통행 이상이 있습니다.")
                                .build();
                    }
                }
            }
        }
        return null;
    }

    public AccidentData isNonStop(List<BusArriveInfoData> busArriveInfoDataList) {
        for (BusArriveInfoData busArriveInfoData : busArriveInfoDataList) {
            if (busArriveInfoData.isStationNonStop())
                return AccidentData.builder()
                        .stationId(busArriveInfoData.getStationId())
                        .message("해당 정류장에서 우회중입니다.")
                        .build();
        }
        return null;
    }

    public List<Integer> createBusLocatedStationOrderList(List<BusArriveInfoData> busArriveInfoDataList) {
        List<Integer> busLocatedStationOrderList = new ArrayList<>();

        busLocatedStationOrderList.add(busArriveInfoDataList.get(0).getSecondArrivalStationOrder());
        busLocatedStationOrderList.add(busArriveInfoDataList.get(0).getFirstArrivalStationOrder());

        for (int i = 1; i < busArriveInfoDataList.size(); i++) {
            BusArriveInfoData curBusStation = busArriveInfoDataList.get(i);

            int firstArrivalStationOrder = curBusStation.getFirstArrivalStationOrder();
            int secondArrivalStationOrder = curBusStation.getSecondArrivalStationOrder();

            if (firstArrivalStationOrder == curBusStation.getStationOrder()) {
                busLocatedStationOrderList.add(firstArrivalStationOrder);
                if (secondArrivalStationOrder == curBusStation.getStationOrder()) {
                    busLocatedStationOrderList.add(secondArrivalStationOrder);
                }
            }
        }

        return busLocatedStationOrderList;
    }

//    public static void main(String[] args) {
//        XmlApiUtil xmlApiUtil1 = new XmlApiUtil();
//        BusArriveInfoApi busArriveInfoApi = new BusArriveInfoApi(xmlApiUtil1);
//
//        System.out.println(busArriveInfoApi.predictTrafficIssue());
//    }
}
