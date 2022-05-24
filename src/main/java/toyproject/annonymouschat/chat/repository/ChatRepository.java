package toyproject.annonymouschat.chat.repository;

import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;

import java.util.List;

public interface ChatRepository {
    public Long save(ChatSaveDto chatSaveDto);

    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId);

    public Chat findByChatId(Long id);

    public Chat getRandom(Long userId);

    public void delete(Long id);
}
