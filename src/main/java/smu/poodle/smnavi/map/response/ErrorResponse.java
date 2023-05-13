package smu.poodle.smnavi.map.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.FieldError;

import java.util.List;

@SuperBuilder
@Getter
public class ErrorResponse extends BaseResponse{

    private final String statusCode;

    //해당 List 가 NON_EMPTY 일 때만 응답 JSON 에 포함
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;


    //@Valid 에 의한 에러가 발생했을 때 출력할 에러
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError){
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
