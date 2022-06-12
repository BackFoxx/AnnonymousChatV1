package toyproject.annonymouschat.User.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserSession userSession;

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity duplicateUser(DuplicateUserException e) {
        UserResponseDto userResponseDto = new UserResponseDto(false, e.getMessage());
        return new ResponseEntity(userResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/login/registration")
    public ResponseEntity registration(@Validated @RequestBody UserRegistrationDto registrationDto,
                                       BindingResult bindingResult,
                                       HttpServletResponse response) throws BindException {

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            throw new BindException(bindingResult);
        }

        String savedEmail = userService.registration(registrationDto);

        UserResponseDto userResponseDto = new UserResponseDto(true, "회원가입 완료되었습니다.");
        return new ResponseEntity(userResponseDto, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public String login(@Validated @RequestBody UserLoginDto userLoginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        User loginUser = userService.login(userLoginDto);
        if (loginUser != null) {
            userSession.createSession(loginUser, response);
            return "성공";
        } else {
            log.info("로그인 실패");
            return "실패";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        userSession.expire(request, response);
        return "redirect:/";
    }
}
