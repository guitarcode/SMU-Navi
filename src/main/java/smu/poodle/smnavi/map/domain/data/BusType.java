package smu.poodle.smnavi.map.domain.data;

import lombok.Getter;

@Getter
public enum BusType {
    NORMAL("일반", 1),
    VILLAGE("마을버스", 3),
    BLUE("간선", 11),
    GREEN("지선", 12),
    ;

    private final String typeName;
    private final Integer typeNumber;

    public static BusType fromTypeNumber(Integer typeNumber) {
        for (BusType busType : BusType.values()) {
            if (busType.getTypeNumber().equals(typeNumber)) {
                return busType;
            }
        }
        throw new IllegalArgumentException("Invalid BusType typeNumber: " + typeNumber);
    }

    BusType(String typeName, Integer typeNumber) {
        this.typeName = typeName;
        this.typeNumber = typeNumber;
    }
}
