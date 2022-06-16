package toyproject.annonymouschat.chat.repository;

import toyproject.annonymouschat.chat.model.Chat;

import java.util.List;

public interface ChatRepository {
    Long save(Chat chat);

    List<Chat> findAllByUserId(Long userId);

    Chat findByChatId(Long id);

    Chat getRandom(Long userId);

    void delete(Long id);
}
