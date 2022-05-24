package toyproject.annonymouschat.config.controller.viewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
* Myview 생성자: 이동한 페이지 경로를 저장
* render: 저장한 경로로 forward
* */
public class MyForwardView {

    private final String viewPath;
    public MyForwardView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(Map<String, Object> models, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        models.forEach((key, value) -> request.setAttribute(key, value));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }
}
