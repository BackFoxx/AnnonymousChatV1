package toyproject.annonymouschat.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.chat.dto.ChatDeleteDto;
import toyproject.annonymouschat.chat.dto.ChatPostSaveDeleteResponseDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.service.ChatService;
import toyproject.annonymouschat.config.exception.WrongFormException;

@Slf4j
@RestController
@RequestMapping("/v/")
public class ChatController {
    private ChatService chatService = new ChatService();

    @GetMapping("chat/postbox/random")
    public Chat getRandom(@RequestAttribute("user") User user) {
        try {
            return chatService.getRandom(user.getId());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @PostMapping("chat/post/delete")
    public ChatPostSaveDeleteResponseDto delete(@RequestBody ChatDeleteDto dto) {
        chatService.delete(dto);
        return new ChatPostSaveDeleteResponseDto(true, "삭제 완료되었습니다");
    }

    @PostMapping("chat/post/save")
    public ResponseEntity save(@RequestBody ChatSaveDto dto, @RequestAttribute("user") User user) {
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

            chatService.save(dto);

            ChatPostSaveDeleteResponseDto responseDto = new ChatPostSaveDeleteResponseDto(true, "저장 완료되었습니다.");
            return new ResponseEntity(responseDto, HttpStatus.OK);
        } catch (WrongFormException e) {
            ChatPostSaveDeleteResponseDto responseDto = new ChatPostSaveDeleteResponseDto(false, e.getMessage());
            return new ResponseEntity(responseDto, HttpStatus.BAD_REQUEST);
        }
    }
}
