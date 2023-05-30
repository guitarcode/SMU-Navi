package smu.poodle.smnavi.user.auth;

import lombok.Getter;

@Getter
public enum Kind { //사고 종류
    DEMO(1, "시위"),
    ACCIDENT(2, "사고"),
    BUS_FULL(3, "만차"),
    BYPASS(4, "우회"),
    CATEGORY_ETC(5, "기타");
    private final int kindNumber;
    private final String kindExplain;

    Kind(int kindNumber, String kindExplain) {
        this.kindNumber = kindNumber;
        this.kindExplain = kindExplain;
    }

    public static Kind getKindByNumber(int number) {
        for (Kind kind : Kind.values()) {
            if (kind.kindNumber == number) {
                return kind;
            }
        }
        //todo: CustomException 만들기
        throw new IllegalArgumentException("Invalid Kind number: " + number);
    }
}