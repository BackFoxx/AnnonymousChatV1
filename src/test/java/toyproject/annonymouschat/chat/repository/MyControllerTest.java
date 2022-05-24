package toyproject.annonymouschat.chat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
@Slf4j
@MyController
@MyRequestMapping(value = "/v2/mapping", method = RequestMethod.GET)
public class MyControllerTest {

    @MyRequestMapping(value = "/v2/test", method = RequestMethod.GET)
    public void mappingMethodTest() {
        log.info("야 눈이다!");
    }

    @MyRequestMapping(value = "/v4/test", method = RequestMethod.GET)
    public void mappingMethodTest32() {
        log.info("야 눈이다!!");
    }
}
