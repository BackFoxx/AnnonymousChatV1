package toyproject.annonymouschat.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.chat.dto.ChatPostSaveDeleteResponseDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.service.ChatService;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.exception.WrongFormException;
import toyproject.annonymouschat.config.controller.controller.ControllerResponseJson;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping(value = "/v/chat/post/save", method = RequestMethod.POST)
public class ChatPostSaveServlet implements ControllerResponseJson {
    private ChatService chatService = new ChatService();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.JSON)
    public ChatPostSaveDeleteResponseDto process(Map<String, Object> requestParameters) {
        ServletInputStream requestBody = (ServletInputStream) requestParameters.get("requestBody");
        try {
            ChatSaveDto dto = objectMapper.readValue(requestBody, ChatSaveDto.class);
            dto.setUserId(((User) requestParameters.get("user")).getId());

            // 검증 로직
            if (dto.getUserId() == null) {
                throw new WrongFormException("로그인이 안 되어있는 듯 합니다");
            }
            if (dto.getContent().isEmpty()) {
                throw new WrongFormException("내용을 입력해주세요");
            }
            if (dto.getContent().length() > 140) {
                throw new WrongFormException("내용은 140자를 넘지 않게 해주세요");
            }


            chatService.save(dto);

            return new ChatPostSaveDeleteResponseDto(true, "저장 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WrongFormException e) {
            return new ChatPostSaveDeleteResponseDto(false, e.getMessage());
        }
    }
}
