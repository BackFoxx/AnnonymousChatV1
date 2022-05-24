package toyproject.annonymouschat.chat.controller;

import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.service.ChatService;
import toyproject.annonymouschat.config.controller.controller.ControllerResponseJson;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping("/v/chat/postbox/random")
public class ChatGetRandomServlet implements ControllerResponseJson {
    private ChatService chatService = new ChatService();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.JSON)
    public Chat process(Map<String, Object> requestParameters) {
        Long userId = ((User) requestParameters.get("user")).getId();
        Chat randomChat = chatService.getRandom(userId);
        return randomChat;
    }
}
