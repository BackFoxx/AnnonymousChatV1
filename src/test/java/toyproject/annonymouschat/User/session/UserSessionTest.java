package toyproject.annonymouschat.User.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import toyproject.annonymouschat.User.entity.User;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

class UserSessionTest {

    UserSession userSession = new UserSession();

    @Test
    @DisplayName("세션 생성_성공")
    void createSession_success() {
        // given
        User user = new User(1L, "test@gmail.com", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        this.userSession.createSession(user, response);

        // then
        assertThat(response.getCookie("SessionId")).isNotNull();
        assertThat(response.getCookie("SessionId").getMaxAge()).isEqualTo(30);
        assertThat(response.getCookie("SessionId").getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("쿠키 찾기_성공")
    void findCookie_success() {
        String cookieValue = "A000-0A00-00A0-000A";
        String cookieName = "SessionId";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(cookieName, cookieValue));

        Cookie findCookie = this.userSession.findCookie(request, cookieName);
        assertThat(findCookie.getName()).isEqualTo(cookieName);
        assertThat(findCookie.getValue()).isEqualTo(cookieValue);
    }

    @Test
    @DisplayName("쿠키 찾기_실패 (쿠키 자체가 없음)")
    void findCookie_fail_noCookie() {
        String cookieName = "SessionId";
        MockHttpServletRequest request = new MockHttpServletRequest();

        Cookie findCookie = this.userSession.findCookie(request, cookieName);
        assertThat(findCookie).isNull();
    }

    @Test
    @DisplayName("쿠키 찾기_실패 (원하는 쿠키가 없음)")
    void findCookie_fail_notfound() {
        String cookieName = "SessionId";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("A", "I ain't cookie you want!"));
        request.setCookies(new Cookie("B", "I ain't cookie you want!"));
        request.setCookies(new Cookie("C", "I ain't cookie you want!"));

        Cookie findCookie = this.userSession.findCookie(request, cookieName);
        assertThat(findCookie).isNull();
    }

    @Test
    @DisplayName("세션 찾기_성공")
    void getSession_success() {
        // given
        User user = new User(1L, "test@gmail.com", "password");

        MockHttpServletResponse response = new MockHttpServletResponse();
        this.userSession.createSession(user, response);

        String cookieValue = response.getCookie("SessionId").getValue();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("SessionId", cookieValue));

        // when
        User findSessionValue = this.userSession.getSession(request);
        assertThat(findSessionValue).isEqualTo(user);
    }

    @Test
    @DisplayName("세션 찾기_실패 (원하는 쿠키가 없음)")
    void getSession_fail_notFound() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Not", "I am not the one"));

        // when
        User findSessionValue = this.userSession.getSession(request);
        assertThat(findSessionValue).isNull();
    }

    @Test
    @DisplayName("세션 삭제_성공")
    void expire_success() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String sessionId = "SessionId";
        request.setCookies(new Cookie(sessionId, "A000-0A00-00A0-000A"));

        // when
        this.userSession.expire(request, response);

        // then
        assertThat(response.getCookie(sessionId).getMaxAge()).isEqualTo(0);
    }
}