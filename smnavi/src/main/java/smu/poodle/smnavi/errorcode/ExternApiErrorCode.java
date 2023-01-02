package smu.poodle.smnavi.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExternApiErrorCode implements ErrorCode{

    UNSUPPORTED_OR_INVALID_GPS_POINTS(HttpStatus.BAD_REQUEST, "해당 지점은 길찾기가 지원되지 않거나 잘못된 위도, 경도 값입니다."),
    NO_PATH_FOUND(HttpStatus.NO_CONTENT, "검색된 경로가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
