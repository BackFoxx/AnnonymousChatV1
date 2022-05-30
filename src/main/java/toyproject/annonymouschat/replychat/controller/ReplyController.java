package toyproject.annonymouschat.replychat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.service.ReplyChatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v")
public class ReplyController {

    private final ReplyChatService replyChatService;

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
    public ResponseEntity saveReply(@RequestAttribute("user") User user,
                                    @Validated @RequestBody ReplyChatSaveDto dto,
                                    BindingResult bindingResult) {
        dto.setUserId(user.getId());

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            ReplyChatSaveDeleteResponseDto userResponseDto = new ReplyChatSaveDeleteResponseDto(false, bindingResult.getAllErrors().get(0).getCode());
            return new ResponseEntity<ReplyChatSaveDeleteResponseDto>(userResponseDto, HttpStatus.BAD_REQUEST);
        }

        replyChatService.saveReply(dto);

        log.info("reply 저장 완료");
        ReplyChatSaveDeleteResponseDto responseDto = new ReplyChatSaveDeleteResponseDto(true, "저장 완료되었습니다");
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
