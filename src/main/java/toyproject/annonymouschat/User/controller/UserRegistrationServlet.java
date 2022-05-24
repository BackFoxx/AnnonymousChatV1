package toyproject.annonymouschat.User.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.dto.UserResponseDto;
import toyproject.annonymouschat.User.service.UserService;
import toyproject.annonymouschat.config.controller.customAnnotation.*;
import toyproject.annonymouschat.config.exception.WrongFormException;
import toyproject.annonymouschat.config.controller.controller.ControllerAutoJson;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Slf4j
@MyController
@MyRequestMapping(value = "/v/login/registration", method = RequestMethod.POST)
public class UserRegistrationServlet implements ControllerAutoJson<UserRegistrationDto> {
    private UserService userService = new UserService();
    @MyHttpResponse
    HttpServletResponse response;

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.JSON)
    public UserResponseDto process(@MyRequestBody UserRegistrationDto registrationDto) {

        try {
            if (!Pattern.compile("^(.+)@(.+)$").matcher(registrationDto.getUserEmail()).matches()) {
                throw new WrongFormException("이메일 형식이 아닙니다.");
            }
            if (registrationDto.getUserEmail().isEmpty()) {
                throw new WrongFormException("이메일 주소를 입력해주세요");
            }
            if (!Pattern.compile("^.{4,}").matcher(registrationDto.getPassword()).matches()) {
                throw new WrongFormException("비밀번호는 4자 이상이어야 합니다");
            }
            if (registrationDto.getUserEmail().length() > 20) {
                throw new WrongFormException("비밀번호는 20자가 넘어가지 않게 해주세요");
            }

            String savedEmail = userService.registration(registrationDto);

            Cookie registerEmailCookie = new Cookie("registerEmail", savedEmail);
            registerEmailCookie.setPath("/v/login/login-form");
            registerEmailCookie.setMaxAge(5);
            response.addCookie(registerEmailCookie);

            return new UserResponseDto(true, "회원가입 완료되었습니다.");
        } catch (WrongFormException e) {
            return new UserResponseDto(false, e.getMessage());
        }
    }
}
