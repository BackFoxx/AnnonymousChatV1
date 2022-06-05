package toyproject.annonymouschat.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.service.ChatService;
import toyproject.annonymouschat.replychat.dto.RepliesByUserIdDto;
import toyproject.annonymouschat.replychat.dto.RepliesByUserIdResponseDto;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v/")
@Controller
public class WebController {

    private final ChatService chatService;
    private final ReplyChatService replyChatService;

    @RequestMapping
    public String index() {
        log.info("jsp 호출");
        return "index";
    }

    // User
    @RequestMapping("login/login-form")
    public String loginForm(@CookieValue(value = "registerEmail", required = false) String registerEmail, Model model) {
        model.addAttribute("registerEmail", registerEmail);
        log.info("jsp 호출");
        return "login/login-form";
    }

    @RequestMapping("login/registration-form")
    public String registrationForm() {
        log.info("jsp 호출");
        return "login/registration-form";
    }

    //Chat
    @GetMapping("chat/post")
    public String ChatPostForm() {
        log.info("jsp 호출");
        return "chat/post";
    }

    @GetMapping("chat/postbox")
    public String ChatPostBox() {
        log.info("jsp 호출");
        return "chat/postbox";
    }

    @GetMapping("chat/mypostbox")
    public String myChat(@RequestAttribute("user") User user, Model model) {
        log.info("jsp 호출");
        Long userId = user.getId();
        List<MyChatPostBoxResponseDto> findChats = chatService.findAllByUserId(userId);

        model.addAttribute("findChats", findChats);
        return "chat/mypostbox";
    }

    //Reply
    @GetMapping("chat/myreply")
    public String myReply(@RequestAttribute("user") User user, Model model) {
        RepliesByUserIdDto dto = new RepliesByUserIdDto();
        dto.setUserId(user.getId());

        List<RepliesByUserIdResponseDto> replies = replyChatService.findAllByUserId(dto);
        model.addAttribute("replies", replies);

        return "chat/replychat/myreplies";
    }

    @GetMapping("replyForm")
    public String replyForm(@RequestParam Long id, Model model) {
        Chat chat = chatService.findByChatId(id);
        model.addAttribute("chat", chat);
        return "chat/replychat/replychat-form";
    }
}
