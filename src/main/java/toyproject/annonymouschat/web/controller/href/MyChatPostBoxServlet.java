package toyproject.annonymouschat.web.controller.href;

import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.service.ChatService;
import toyproject.annonymouschat.config.controller.controller.ControllerWithTwoMap;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import java.util.List;
import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping("/v/chat/mypostbox")
public class MyChatPostBoxServlet implements ControllerWithTwoMap {

    private ChatService chatService = new ChatService();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.FORWARD)
    public String process(Map<String, Object> requestParameters, Map<String, Object> model) {
        log.info("jsp 호출");
        Long userId = ((User) requestParameters.get("user")).getId();
        List<MyChatPostBoxResponseDto> findChats = chatService.findAllByUserId(userId);

        model.put("findChats", findChats);
        return "chat/mypostbox";
    }
}
