package toyproject.annonymouschat.User.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.config.exception.DuplicateUserException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("회원가입_성공 (가입한 이메일을 반환)")
    @Transactional
    void registration_success() {
        // given
        UserRegistrationDto userDto = new UserRegistrationDto();
        String userEmail = "test@gmail.com";
        userDto.setUserEmail(userEmail);
        userDto.setPassword("testpassword");

        // when
        String registrationUserEmail = userService.registration(userDto);

        // then
        assertThat(registrationUserEmail).isEqualTo(userEmail);
    }

    @Test
    @DisplayName("회원가입_실패 (이미 가입된 이메일 -> DuplicateUserException throw)")
    @Transactional
    void registration_fail() {
        // given
        UserRegistrationDto userDto = new UserRegistrationDto();
        String userEmail = "test@gmail.com";
        userDto.setUserEmail(userEmail);
        userDto.setPassword("testpassword");

        // when
        userService.registration(userDto);

        // then
        assertThatThrownBy(() -> userService.registration(userDto)) // 중복된 이메일
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage("이미 사용자가 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("로그인_성공 (로그인된 유저 반환)")
    @Transactional
    void login_success() {
        // given
        UserRegistrationDto userDto = new UserRegistrationDto();
        String userEmail = "test@gmail.com";
        userDto.setUserEmail(userEmail);
        String password = "testpassword";
        userDto.setPassword(password);

        userService.registration(userDto);

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserEmail(userEmail);
        userLoginDto.setPassword(password);

        // when
        User loginUser = userService.login(userLoginDto);

        // then
        assertThat(loginUser.getUserEmail()).isEqualTo(userEmail);
        assertThat(loginUser.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("로그인_실패 (존재하지 않는 사용자 -> NoSuchElementException throw)")
    @Transactional
    void login_fail_NoUser() {
        // given
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserEmail("NoExist@gmail.com");
        userLoginDto.setPassword("nono");

        // when
        assertThatThrownBy(() -> userService.login(userLoginDto))
                // then
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("로그인_실패 (틀린 비밀번호 -> NoSuchElementException throw)")
    @Transactional
    void login_fail_WrongPSW() {
        // given

        // given
        UserRegistrationDto userDto = new UserRegistrationDto();
        String userEmail = "test@gmail.com";
        userDto.setUserEmail(userEmail);
        userDto.setPassword("testpassword");

        userService.registration(userDto);

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserEmail(userEmail);
        userLoginDto.setPassword("nono");

        // when
        assertThatThrownBy(() -> userService.login(userLoginDto))
                // then
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("비밀번호가 틀립니다.");
    }
}