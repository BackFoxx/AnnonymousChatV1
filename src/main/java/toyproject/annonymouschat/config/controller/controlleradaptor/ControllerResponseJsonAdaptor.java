package toyproject.annonymouschat.config.controller.controlleradaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import toyproject.annonymouschat.config.controller.controller.ControllerResponseJson;
import toyproject.annonymouschat.config.controller.ModelView;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ControllerResponseJsonAdaptor implements ControllerAdaptor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(Object controller) {
        if (controller instanceof ControllerResponseJson) {
            try {
                Method process = Arrays.stream(controller.getClass().getSuperclass().getDeclaredMethods())
                        .filter(method -> method.getName().equals("process")).findAny().orElseThrow(() -> new NoSuchMethodException());
                return process.getAnnotation(ReturnType.class).type() == ReturnType.ReturnTypes.JSON;
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object controller) {
        ControllerResponseJson target = (ControllerResponseJson) controller;
        Map<String, Object> requestParameters = setRequestParametersToMap(request, response);
        Object result = target.process(requestParameters);

        ModelView modelView = new ModelView();
        modelView.getModel().put("response", result);
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
