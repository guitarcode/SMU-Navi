package smu.poodle.smnavi.info.domain;

import lombok.Getter;
import smu.poodle.smnavi.map.domain.data.TransitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum Location {
    BUS_GWANGHWAMUN("100000023", TransitType.BUS, "KT광화문지사"), BUS_GYEONGBOKGUNG("100000021", TransitType.BUS, "경복궁역"),
    BUS_CITYHALL("101000033",TransitType.BUS, "시청"),
    SUB_GWANGHWAMUN("533", TransitType.SUBWAY,"광화문 5호선"), SUB_GYEONGBOKGUNG("327",TransitType.SUBWAY,"경복궁 3호선"), SUB_CITYHALL_1("132",TransitType.SUBWAY,"시청역 1호선");
    private final String stationId;
    private final TransitType transitType;
    private final String stationName;

    private static final Map<TransitType,List<Location>> LOCATION_MAP = new HashMap<>();
    static{ //클래스가 로드될 때 실행
        for(TransitType transitType : TransitType.values()){
            LOCATION_MAP.put(transitType, new ArrayList<>());
        }
        for(Location location : Location.values()){
            LOCATION_MAP.get(location.getTransitType()).add(location);
        }
    }
    Location(String stationId, TransitType transitType, String stationName) {
        this.stationId = stationId;
        this.transitType = transitType;
        this.stationName = stationName;
    }
    public static List<Location> getByTransitType(TransitType transitType){
        return LOCATION_MAP.get(transitType);
    }
}