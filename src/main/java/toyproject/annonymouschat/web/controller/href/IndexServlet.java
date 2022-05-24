package toyproject.annonymouschat.web.controller.href;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.config.controller.controller.ControllerWithTwoMap;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping(value = "/v/", method = RequestMethod.GET)
public class IndexServlet implements ControllerWithTwoMap {
    @Override
    @ReturnType(type = ReturnType.ReturnTypes.FORWARD)
    public String process(Map<String, Object> requestParameters, Map<String, Object> model) {
        log.info("jsp 호출");
        return "index";
    }
}
