package toyproject.annonymouschat.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.chat.dto.*;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.service.ChatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/postbox")
    public GetRandomResponseDto getRandom(@RequestAttribute("user") User user) {
        try {
            Chat randomChat = chatService.getRandom(user.getId());
            if (randomChat == null) return null;

            return GetRandomResponseDto.toDto(randomChat);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @PostMapping("/delete")
    public ChatPostSaveDeleteResponseDto delete(@RequestBody ChatDeleteDto dto) {
        chatService.delete(dto);
        return new ChatPostSaveDeleteResponseDto(true, "삭제 완료되었습니다");
    }

    @PostMapping("/save")
    public ResponseEntity save(@Validated @RequestBody ChatSaveDto dto,
                               BindingResult bindingResult,
                               @RequestAttribute("user") User user) throws BindException {
        dto.setUserId(user.getId());

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        chatService.save(dto);

        ChatPostSaveDeleteResponseDto responseDto = new ChatPostSaveDeleteResponseDto(true, "저장 완료되었습니다.");
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @GetMapping("/myChat")
    public List<MyChatPostBoxResponseDto> myChat(@RequestAttribute("user") User user) {
        Long userId = user.getId();
        return chatService.findAllByUserId(userId);
    }
}
