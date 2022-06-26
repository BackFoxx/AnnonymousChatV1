package toyproject.annonymouschat.replychat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyChatService replyChatService;

    @GetMapping("/replies/{chatId}")
    public ResponseEntity getAllReplyByChatId(@PathVariable Long chatId) {
        List<RepliesByChatIdResponseDto> findChats = replyChatService.findAllByChatId(chatId);
        return new ResponseEntity<>(findChats, HttpStatus.OK);
    }

    @GetMapping("/my-reply")
    public List<RepliesByUserIdResponseDto> myReply(@RequestAttribute("user") User user) {
        RepliesByUserIdDto dto = new RepliesByUserIdDto();
        dto.setUserId(user.getId());

        return replyChatService.findAllByUserId(dto);
    }

    @GetMapping("/my-reply/info/{reply_id}")
    public ReplyInfo replyInfo(@PathVariable Long reply_id) {
        return replyChatService.replyInfo(reply_id);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteReply(@RequestBody ReplyDeleteDto replyDeleteDto) {
        replyChatService.deleteReply(replyDeleteDto);
        ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(true, "삭제 완료되었습니다");
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity saveReply(@RequestAttribute("user") User user,
                                    @Validated @RequestBody ReplyChatSaveDto dto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            throw new BindException(bindingResult);
        }

        dto.setUserId(user.getId());
        replyChatService.saveReply(dto);

        log.info("reply 저장 완료");
        ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(true, "저장 완료되었습니다");
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
