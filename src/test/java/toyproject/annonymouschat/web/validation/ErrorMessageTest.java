package toyproject.annonymouschat.web.validation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ErrorMessageTest {

    @Autowired
    MessageSource messageSource;

    @Test
    @DisplayName("메시지 잘 읽어들이는지 테스트")
    void messsageTest() {
        String hello = messageSource.getMessage("hello", null, null);
        assertThat(hello).isEqualTo("안녕");
    }

    @Test
    @DisplayName("에러 메시지 잘 읽어들이는지 테스트")
    void errorMessageTest() {
        String basic = messageSource.getMessage("basic", null, null);
        assertThat(basic).isEqualTo("베이직 에러");
    }

    @Test
//    @Disabled
    @DisplayName("bean validation 실패 메시지 테스트")
    void errorMessageTest2() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUserEmail(null);
        dto.setPassword("password");

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);
        violations.stream().forEach((violation) -> {
            System.out.println(violation.getMessage());
        });
    }
}
