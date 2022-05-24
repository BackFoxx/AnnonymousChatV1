package toyproject.annonymouschat.web.controller.href;

import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.config.controller.controller.ControllerWithTwoMap;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;
import toyproject.annonymouschat.replychat.dto.RepliesByUserIdDto;
import toyproject.annonymouschat.replychat.dto.RepliesByUserIdResponseDto;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import java.util.List;
import java.util.Map;

@MyController
@MyRequestMapping("/v/chat/myreply")
public class MyRepliesServlet implements ControllerWithTwoMap {
    ReplyChatService replyChatService = new ReplyChatService();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.FORWARD)
    public String process(Map<String, Object> requestParameters, Map<String, Object> model) {
        RepliesByUserIdDto dto = new RepliesByUserIdDto();
        dto.setUserId(((User) requestParameters.get("user")).getId());

        List<RepliesByUserIdResponseDto> replies = replyChatService.findAllByUserId(dto);

        model.put("replies", replies);
        return "chat/replychat/myreplies";
    }
}
