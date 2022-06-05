package toyproject.annonymouschat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;

@SpringBootTest
public class MessageTest {
    @Autowired
    MessageSource messageSource;

    @Test
    void messageTest() {
        String notBlank = messageSource.getMessage("NotBlank", null, null);
        Assertions.assertThat(notBlank).isEqualTo("값이 비어있거나 공백이 있으면 안돼요");
    }
}
