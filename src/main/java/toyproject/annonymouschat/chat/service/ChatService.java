package toyproject.annonymouschat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.annonymouschat.chat.dto.ChatDeleteDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    public Chat save(ChatSaveDto chatSaveDto) {
        Chat chat = new Chat();
        chat.setContent(chatSaveDto.getContent());
        chat.setUserId(chatSaveDto.getUserId());

        Long saveId = repository.save(chat);
        return repository.findByChatId(saveId);
    }
    public void delete(ChatDeleteDto chatDeleteDto) {
        repository.delete(chatDeleteDto.getId());
    }
    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId) {
        List<Chat> findChatList = repository.findAllByUserId(userId);

        List<MyChatPostBoxResponseDto> responseList = new ArrayList<>();
        findChatList.forEach(chat -> {
            MyChatPostBoxResponseDto dto = new MyChatPostBoxResponseDto();
            dto.setId(chat.getId());
            dto.setContent(chat.getContent());
            dto.setCreateDate(chat.getCreateDate());

            responseList.add(dto);
        });

        return responseList;
    }
    public Chat getRandom(Long userId) {
        return repository.getRandom(userId);
    }
    public Chat findByChatId(Long id) {
        return repository.findByChatId(id);
    }
}
