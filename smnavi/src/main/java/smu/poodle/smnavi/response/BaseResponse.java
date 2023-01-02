package smu.poodle.smnavi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@SuperBuilder
@Getter
public abstract class BaseResponse {
    private final String message;
}
