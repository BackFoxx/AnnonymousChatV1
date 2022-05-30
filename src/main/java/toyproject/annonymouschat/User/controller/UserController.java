package toyproject.annonymouschat.User.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.dto.UserResponseDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.service.UserService;
import toyproject.annonymouschat.User.session.UserSession;
import toyproject.annonymouschat.config.exception.DuplicateUserException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/v")
public class UserController {

    private UserService userService = new UserService();
    private UserSession userSession = new UserSession();

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity duplicateUser(DuplicateUserException e) {
        UserResponseDto userResponseDto = new UserResponseDto(false, e.getMessage());
        return new ResponseEntity(userResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/login/registration")
    public ResponseEntity registration(@Validated @RequestBody UserRegistrationDto registrationDto,
                                       BindingResult bindingResult,
                                       HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            UserResponseDto userResponseDto = new UserResponseDto(false, bindingResult.getAllErrors().get(0).getCode());
            return new ResponseEntity<UserResponseDto>(userResponseDto, HttpStatus.BAD_REQUEST);
        }

        String savedEmail = userService.registration(registrationDto);

        Cookie registerEmailCookie = new Cookie("registerEmail", savedEmail);
        registerEmailCookie.setPath("/v/login/login-form");
        registerEmailCookie.setMaxAge(5);
        response.addCookie(registerEmailCookie);

        UserResponseDto userResponseDto = new UserResponseDto(true, "회원가입 완료되었습니다.");
        return new ResponseEntity(userResponseDto, HttpStatus.OK);

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
