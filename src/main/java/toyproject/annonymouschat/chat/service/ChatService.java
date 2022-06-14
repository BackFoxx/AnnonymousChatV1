package toyproject.annonymouschat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.annonymouschat.chat.dto.ChatDeleteDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    public Chat save(ChatSaveDto chatSaveDto) {
        Long saveId = repository.save(chatSaveDto);
        return repository.findByChatId(saveId);
    }
    public void delete(ChatDeleteDto chatDeleteDto) {
        repository.delete(chatDeleteDto.getId());
    }
    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }
    public Chat getRandom(Long userId) {
        return repository.getRandom(userId);
    }
    public Chat findByChatId(Long id) {
        return repository.findByChatId(id);
    }
}
