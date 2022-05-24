package toyproject.annonymouschat.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.session.UserSession;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class UnLoginnedFilter implements Filter {
    /*
     * 로그인되어있지 않아도 리다이렉트하지 않고
     * attribute에 유저값을 null로 넣고 정상 진행하는 필터
     *
     * 필터가 적용되는 URL들은 WebConfig에 등록돼 있다.
     */
    private UserSession userSession = new UserSession();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("UnLoginnedFilter 작동중");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User user = userSession.getSession(httpRequest);
        log.info("user = {}", user);
        if (user == null) {
            httpRequest.setAttribute("user", null); // user가 없으면 null을 넣고
        } else {
            httpRequest.setAttribute("user", user); // user가 있으면 user를 넣는다
        }
        chain.doFilter(httpRequest, response);
    }

}
