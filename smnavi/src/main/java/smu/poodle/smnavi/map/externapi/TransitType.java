package smu.poodle.smnavi.map.externapi;

public enum TransitType {
    SUBWAY, BUS, WALK;
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
