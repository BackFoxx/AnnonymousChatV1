package toyproject.annonymouschat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.model.User;

@Slf4j
@Controller
public class WebController {
    @ResponseBody
    @GetMapping("/")
    public User index(@RequestAttribute(required = false) User user) {
        return user;
    }
}
