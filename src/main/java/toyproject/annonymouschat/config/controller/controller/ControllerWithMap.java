package toyproject.annonymouschat.config.controller.controller;

import toyproject.annonymouschat.config.controller.ModelView;

import java.util.Map;

public interface ControllerWithMap {
    ModelView process(Map<String, Object> requestParameters);
}
