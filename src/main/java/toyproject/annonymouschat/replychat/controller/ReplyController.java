package toyproject.annonymouschat.replychat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.config.controller.ModelView;
import toyproject.annonymouschat.config.exception.WrongFormException;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v")
public class ReplyController {
    private ReplyChatService replyChatService = new ReplyChatService();

    @GetMapping("/reply/find")
    public ResponseEntity repliesByChatId(@ModelAttribute RepliesByChatIdDto dto) {
        List<RepliesByChatIdResponseDto> findChats = replyChatService.findAllByChatId(dto);
        return new ResponseEntity<>(findChats, HttpStatus.OK);
    }

    @PostMapping("/reply/delete")
    public ResponseEntity deleteReply(@RequestBody ReplyDeleteDto replyDeleteDto) {
        replyChatService.deleteReply(replyDeleteDto);
        ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(true, "삭제 완료되었습니다");
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @PostMapping("/reply/save")
    public ResponseEntity saveReply(@RequestAttribute("user") User user, @RequestBody ReplyChatSaveDto dto) {
        dto.setUserId(user.getId());

        try {
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
            return new ResponseEntity(responseDto, HttpStatus.OK);
        } catch (WrongFormException e) {
            ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(false, e.getMessage());
            return new ResponseEntity(responseDto, HttpStatus.BAD_REQUEST);
        }
    }
}
