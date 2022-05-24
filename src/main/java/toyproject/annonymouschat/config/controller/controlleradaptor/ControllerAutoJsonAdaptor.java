package toyproject.annonymouschat.config.controller.controlleradaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.config.controller.controller.ControllerAutoJson;
import toyproject.annonymouschat.config.controller.ModelView;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestBody;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;
import toyproject.annonymouschat.config.controller.customAnnotation.MyHttpRequest;
import toyproject.annonymouschat.config.controller.customAnnotation.MyHttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Slf4j
public class ControllerAutoJsonAdaptor implements ControllerAdaptor {
    @Override
    public boolean supports(Object controller) {
        if (controller instanceof ControllerAutoJson) {
            Method process = getMethod(controller);
            return process.getAnnotation(ReturnType.class).type() == ReturnType.ReturnTypes.JSON;
        }
        return false;
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object controller) {
        Method method = getMethod(controller);

        Field[] fields = controller.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> setFieldInjection(controller, field, request, response));

        Parameter[] parameters = method.getParameters();
        Parameter parameter = Arrays.stream(parameters)
                .filter(param -> param.isAnnotationPresent(MyRequestBody.class)).findAny()
                .orElseThrow(() -> new RuntimeException("파라미터 없음"));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object requestBody = objectMapper.readValue(request.getInputStream(), parameter.getType());

            Object model = method.invoke(controller, requestBody);

            ModelView modelView = new ModelView();
            modelView.getModel().put("response", model);

            return modelView;
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getMethod(Object controller) {
        Method process = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("process")).findAny().get();
        return process;
    }

    private void setFieldInjection(Object controller, Field field, HttpServletRequest request, HttpServletResponse response) {
        if (field.isAnnotationPresent(MyHttpRequest.class) && field.getClass().isAssignableFrom(HttpServletRequest.class)) {
            field.setAccessible(true);
            try {
                field.set(controller, request);
            } catch (IllegalAccessException e) {
                log.info("리퀘스트 안들어감");
                return;
            }
        }

        if (field.isAnnotationPresent(MyHttpResponse.class) && field.getType().isAssignableFrom(HttpServletResponse.class)) {
            field.setAccessible(true);
            try {
                field.set(controller, response);
            } catch (IllegalAccessException e) {
                log.info("리퀘스트 안들어감");
                return;
            }
        }


        return;
    }
}
