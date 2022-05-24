package toyproject.annonymouschat.config.controller.controlleradaptor;

import toyproject.annonymouschat.config.controller.controller.ControllerWithMap;
import toyproject.annonymouschat.config.controller.ModelView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ControllerWithMapAdaptor implements ControllerAdaptor {
    @Override
    public boolean supports(Object controller) {
        return controller instanceof ControllerWithMap;
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object controller) {
        ControllerWithMap target = (ControllerWithMap) controller;
        Map<String, Object> requestParameters = setRequestParametersToMap(request, response);

        ModelView modelView = target.process(requestParameters);
        return modelView;
    }

    private Map<String, Object> setRequestParametersToMap(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> requestParameters = new HashMap<>();
        /*
         * request의 attributes를 Map 객체에 담는다.
         * */
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            requestParameters.put(attributeName, request.getAttribute(attributeName));
        }

        /*
         * request의 파라미터를 Map 객체에 담는다,
         * */
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            requestParameters.put(parameterName, request.getParameter(parameterName));
        }

        /*
         * request Body의 내용을 Map 객체의 requestBody 값으로 담는다.
         * */
        try {
            ServletInputStream inputStream = request.getInputStream();
            requestParameters.put("requestBody", inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*
         * request, response 객체도 넣어 선택적으로 쓸 수 있게 해주었다.
         * */
        requestParameters.put("httpServletRequest", request);
        requestParameters.put("httpServletResponse", response);

        return requestParameters;
    }
}
