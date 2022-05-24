package toyproject.annonymouschat.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
class ControllerMappingV1Test {

    @Test
    void controllerMapperV1Test() {
        ControllerMappingV1 controllerMappingV1 = new ControllerMappingV1();
        Map<String, Object> controllerMap = controllerMappingV1.controllerMapper();
        controllerMap.forEach((URI, controller) -> log.info("URI : {}, controller : {}", URI, controller));
    }
}