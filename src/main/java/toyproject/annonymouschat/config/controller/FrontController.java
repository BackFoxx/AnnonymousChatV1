package toyproject.annonymouschat.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import toyproject.annonymouschat.config.controller.controlleradaptor.*;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;
import toyproject.annonymouschat.config.controller.viewResolver.MyForwardView;
import toyproject.annonymouschat.config.controller.viewResolver.MyJson;
import toyproject.annonymouschat.config.controller.viewResolver.MyRedirectView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@WebServlet(urlPatterns = {"/v/*"})
public class FrontController extends HttpServlet {
    Map<String, Object> controllerMap;
    List<ControllerAdaptor> controllerAdaptorList = new ArrayList<>();

    public FrontController() {
        //어노테이션 기반 ControllerMapping
        ControllerMappingV1 controllerMappingV1 = new ControllerMappingV1();
        controllerMap = controllerMappingV1.controllerMapper();

        //Adaptor
        controllerAdaptorList.add(new ControllerWithMapAdaptor());
        controllerAdaptorList.add(new ControllerWithTwoMapAdaptor());
        controllerAdaptorList.add(new ControllerResponseJsonAdaptor());
        controllerAdaptorList.add(new ControllerAutoJsonAdaptor());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("FrontController 호출, URI = {}", requestURI);

        Object controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller = setArgumentResolver(controller);

        ControllerAdaptor adaptor = assignControllerAdaptor(controller);

        log.info("ControllerAdaptor = {}", adaptor.getClass());
        log.info("Controller = {}", controller.getClass());

        ModelView modelView = adaptor.handle(request, response, controller);

        Object result = ReturnTypeResolver(controller, modelView);

        if (result instanceof MyForwardView) ((MyForwardView) result).render(modelView.getModel(), request, response);
        else if (result instanceof MyJson) ((MyJson) result).render(response);
        else if (result instanceof MyRedirectView) ((MyRedirectView) result).render(modelView.getModel(), request, response);

        log.info("--------- 호출 완료! ------------");
    }

    private Object setArgumentResolver(Object controller) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(controller.getClass());
        enhancer.setCallback(new ArgumentResolverV1(controller));
        return enhancer.create();
    }

    private ControllerAdaptor assignControllerAdaptor(Object controller) {
        for (ControllerAdaptor adaptor : controllerAdaptorList) {
            if (adaptor.supports(controller)) return adaptor;
        }
        throw new RuntimeException("잘못된 형식의 컨트롤러");
    }

    private Object ReturnTypeResolver(Object controller, ModelView modelView) {
        /*
        * 컨트롤러의 @ReturnType 어노테이션을 분석하여
        * 컨트롤러가 redirect 하는지, forward 하는지, Json을 응답하는지 판별하여
        * 그에 맞는 다음 단계를 지정한다.
        * */

        try {
            Method superClassMethod = Arrays.stream(controller.getClass().getSuperclass().getDeclaredMethods())
                    .filter(method -> method.getName().equals("process")).findAny()
                    .orElseThrow(() -> new NoSuchMethodException("잘못된 컨트롤러 형식"));

            ReturnType.ReturnTypes returnType = superClassMethod.getAnnotation(ReturnType.class).type();

            if (returnType == ReturnType.ReturnTypes.FORWARD) {
                return new MyForwardView(viewResolver(modelView.getViewName()));
            }
            else if (returnType == ReturnType.ReturnTypes.REDIRECT)
                return new MyRedirectView(modelView.getViewName());
            else if (returnType == ReturnType.ReturnTypes.JSON) {
                return new MyJson(modelView.getModel().get("response"));
            } else {
                throw new RuntimeException("어노테이션 설정 안됨");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String viewResolver(String viewName) {
        return  "/" + viewName + ".jsp";
    }
}
