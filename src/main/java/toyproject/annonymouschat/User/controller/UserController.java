package toyproject.annonymouschat.User.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.dto.UserResponseDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.service.UserService;
import toyproject.annonymouschat.User.session.UserSession;
import toyproject.annonymouschat.config.exception.WrongFormException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/v")
public class UserController {

    private UserService userService = new UserService();
    private UserSession userSession = new UserSession();

    @ResponseBody
    @PostMapping(value = "/login/registration")
    public ResponseEntity registration(@RequestBody UserRegistrationDto registrationDto, HttpServletResponse response) {
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

            UserResponseDto userResponseDto = new UserResponseDto(true, "회원가입 완료되었습니다.");
            return new ResponseEntity(userResponseDto, HttpStatus.OK);
        } catch (WrongFormException e) {
            UserResponseDto userResponseDto = new UserResponseDto(false, e.getMessage());
            return new ResponseEntity<UserResponseDto>(userResponseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute UserLoginDto userLoginDto,
                        HttpServletResponse response) {

        User loginUser = userService.login(userLoginDto);
        if (loginUser != null) {
            userSession.createSession(loginUser, response);
            return "redirect:/";
        } else {
            log.info("로그인 실패");
            return "redirect:/v/login/login-form";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        userSession.expire(request, response);
        return "redirect:/";
    }
}
