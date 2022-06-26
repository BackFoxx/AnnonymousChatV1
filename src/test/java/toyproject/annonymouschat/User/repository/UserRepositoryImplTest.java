package toyproject.annonymouschat.User.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class UserRepositoryImplTest {

    UserRepository userRepository;

    @Autowired
    ApplicationContext applicationContext;

    @BeforeEach
    void setup(ApplicationContext applicationContext) {
        this.userRepository = applicationContext.getBean(UserRepository.class);
    }

    @Test
    @DisplayName("회원가입_성공 (회원가입한 이메일을 반환)")
    @Transactional
    void registration_success() {
        // given
        User user = new User(null, "test@gmail.com", "test");

//        UserRegistrationDto dto = new UserRegistrationDto();
//        String userEmail = "test@nate.com";
//        dto.setUserEmail(userEmail);
//        dto.setPassword("testPassword");

        // when
        String registratedEmail = this.userRepository.registration(user);

        // then
        assertThat(registratedEmail).isEqualTo(user.getUserEmail());
    }

    @Test
    @DisplayName("이메일로 유저 찾기_성공 (찾은 유저를 반환)")
    @Transactional
    void findByUserEmail_success() {
        // given
        String userEmail = "test@nate.com";
        String password = "testPassword";

        User user = new User(null, userEmail, password);

        this.userRepository.registration(user);

        // when
        User findUser = this.userRepository.findByUserEmail(userEmail);

        // then
        assertThat(findUser.getUserEmail()).isEqualTo(userEmail);
        assertThat(findUser.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("이메일로 유저 찾기_실패 (NULL 반환)")
    void findByUserEmail_fail() {
        //given
        User findUser = this.userRepository.findByUserEmail("DoesntExist@nowhere.com");
        assertThat(findUser).isNull();
    }
}