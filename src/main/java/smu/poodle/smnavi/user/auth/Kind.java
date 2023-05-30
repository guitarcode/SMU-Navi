package smu.poodle.smnavi.user.auth;

import lombok.Getter;

@Getter
public enum Kind { //사고 종류
    DEMO(1), ACCIDENT(2), BUS_FULL(3), BYPASS(4), CATEGORY_ETC(5);
    private final int kindNumber;
    Kind(int kindNumber){
        this.kindNumber = kindNumber;
    }
}