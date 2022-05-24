package toyproject.annonymouschat.config.controller.controller;

import java.util.Map;

public interface ControllerResponseJson {
    Object process(Map<String, Object> requestParameters);
}
