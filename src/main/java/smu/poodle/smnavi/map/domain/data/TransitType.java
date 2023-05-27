package smu.poodle.smnavi.map.domain.data;

//todo: Map 을 만들어서 새로운 객체를 생성하지 않도록 관리
public enum TransitType {
    SUBWAY(1), BUS(2), WALK(3);

    private final int typeNumber;

    TransitType(int typeNumber) {
        this.typeNumber = typeNumber;
    }



    public static TransitType of(int typeNumber){
        if(typeNumber == 1){
            return TransitType.SUBWAY;
        }
        else if(typeNumber == 2){
            return TransitType.BUS;
        }
        else if(typeNumber == 3){
            return TransitType.WALK;
        }
        return null;
    }
}
