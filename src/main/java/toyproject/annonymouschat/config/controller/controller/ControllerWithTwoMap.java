package toyproject.annonymouschat.config.controller.controller;

import java.util.Map;

public interface ControllerWithTwoMap {
    String process(Map<String, Object> requestParameters, Map<String, Object> model);
}
