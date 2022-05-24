package toyproject.annonymouschat.User.session;

import toyproject.annonymouschat.User.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserSession {
    public static final String SESSION_COOKIE_NAME = "SessionId";
    private static Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        //세션에 벨류 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //response에 쿠키를 담아 반환
        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        //request가 가진 쿠키들 중 쿠키 네임과 동일한 쿠키를 반환
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    public User getSession(HttpServletRequest request) {
        //찾아낸 쿠키의 value 값에 해당하는 회원정보가 있으면 반환
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return (User)sessionStore.get(sessionCookie.getValue());
    }

    public void expire(HttpServletRequest request, HttpServletResponse response) {
        //찾아낸 쿠키에 해당하는 세션 값을 삭제
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        }
    }
}
