package toyproject.annonymouschat.config.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResult {
    private String field; // 에러가 발생한 필드이름
    private String code; // 에러 코드 (ex. Length)
    private Object rejectedValue; // 에러가 발생한 Value 값
    private String message; // 에러 메시지

    public static ErrorResult create(FieldError fieldError, MessageSource messageSource, Locale locale) {
        return new ErrorResult(
                fieldError.getField(),
                fieldError.getCode(),
                fieldError.getRejectedValue(),
                messageSource.getMessage(fieldError, locale)
        );
    }
}
