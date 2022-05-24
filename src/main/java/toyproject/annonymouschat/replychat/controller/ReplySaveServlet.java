package toyproject.annonymouschat.replychat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;
import toyproject.annonymouschat.config.exception.WrongFormException;
import toyproject.annonymouschat.config.controller.controller.ControllerWithMap;
import toyproject.annonymouschat.config.controller.ModelView;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;
import toyproject.annonymouschat.replychat.dto.ReplyChatSaveDto;
import toyproject.annonymouschat.replychat.dto.ReplyChatSaveDeleteResponseDto;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@MyController
@MyRequestMapping(value = "/v/reply/save", method = RequestMethod.POST)
public class ReplySaveServlet implements ControllerWithMap {
    private ReplyChatService replyChatService = new ReplyChatService();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @ReturnType(type = ReturnType.ReturnTypes.JSON)
    public ModelView process(Map<String, Object> requestParameters) {

        try {
            ReplyChatSaveDto dto = objectMapper.readValue(((ServletInputStream) requestParameters.get("requestBody")), ReplyChatSaveDto.class);
            Long userId = ((User) requestParameters.get("user")).getId();
            dto.setUserId(userId);

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

            replyChatService.saveReply(dto);

            log.info("reply 저장 완료");
            ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(true, "저장 완료되었습니다");

            ModelView modelView = new ModelView();
            modelView.getModel().put("response", responseDto);
            return modelView;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WrongFormException e) {
            ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(false, e.getMessage());
            ModelView modelView = new ModelView();
            modelView.getModel().put("response", responseDto);
            return modelView;
        }
    }
}
