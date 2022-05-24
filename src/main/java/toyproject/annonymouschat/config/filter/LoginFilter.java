package toyproject.annonymouschat.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.session.UserSession;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class LoginFilter implements Filter {
    private UserSession userSession = new UserSession();
    private final String[] whiteLists = {"/", "/v/login", "/v/login/*", "/v/logout", "/css/*", "/js/*", "/favicon.ico"};
    // 필터를 거치지 않는 URI들

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if (isUriNotMatchWhiteList(requestURI)) {
            log.info("LoginFilter 작동중");
            User user = userSession.getSession(httpRequest);
            if (user != null) {
                httpRequest.setAttribute("user", user);
            } else {
                log.info("로그인 안되어있음");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect("/v/login/login-form?redirectURL=" + requestURI);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isUriNotMatchWhiteList(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteLists, requestURI);
    }
}
