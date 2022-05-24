package toyproject.annonymouschat.config.controller.controlleradaptor;

import toyproject.annonymouschat.config.controller.ModelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ControllerAdaptor {
    boolean supports(Object controller); //어댑터에 맞는 컨트롤러인지 확인
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object controller); //컨트롤러를 어댑터에 맞는 형식으로 변환하여 실행
}
