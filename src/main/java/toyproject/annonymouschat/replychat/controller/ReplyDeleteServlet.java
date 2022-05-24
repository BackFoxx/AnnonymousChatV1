package toyproject.annonymouschat.replychat.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.config.controller.controller.ControllerAutoJson;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestBody;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;
import toyproject.annonymouschat.replychat.dto.ReplyChatSaveDeleteResponseDto;
import toyproject.annonymouschat.replychat.dto.ReplyDeleteDto;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

@MyController
@MyRequestMapping(value = "/v/reply/delete", method = RequestMethod.POST)
public class ReplyDeleteServlet implements ControllerAutoJson<ReplyDeleteDto> {
    private ReplyChatService replyChatService = new ReplyChatService();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.JSON)
    public Object process(@MyRequestBody ReplyDeleteDto requestBody) {
        replyChatService.deleteReply(requestBody);
        return new ReplyChatSaveDeleteResponseDto(true, "삭제 완료되었습니다");
    }
}
