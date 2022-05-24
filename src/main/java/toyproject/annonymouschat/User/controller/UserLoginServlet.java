package toyproject.annonymouschat.User.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.service.UserService;
import toyproject.annonymouschat.User.session.UserSession;
import toyproject.annonymouschat.config.controller.controller.ControllerWithMap;
import toyproject.annonymouschat.config.controller.ModelView;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping(value = "/v/login", method = RequestMethod.POST)
public class UserLoginServlet implements ControllerWithMap {
    UserService userService = new UserService();
    UserSession userSession = new UserSession();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.REDIRECT)
    public ModelView process(Map<String, Object> requestParameters) {
        String userEmail = (String) requestParameters.get("userEmail");
        String password = (String) requestParameters.get("password");
        UserLoginDto userLoginDto = new UserLoginDto(userEmail, password);

        User loginUser = userService.login(userLoginDto);
        if (loginUser != null) {
            HttpServletResponse response = (HttpServletResponse) requestParameters.get("httpServletResponse");
            userSession.createSession(loginUser, response);
            return new ModelView("/");
        } else {
            log.info("로그인 실패");
            return new ModelView("/v/login/login-form");
        }
    }

}
