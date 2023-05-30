package smu.poodle.smnavi.map.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@SuperBuilder
@Getter
public abstract class BaseResponse {
    //todo: baseResponse 객체 리팩토링 필요 ㅠ
    private final String message;
}
