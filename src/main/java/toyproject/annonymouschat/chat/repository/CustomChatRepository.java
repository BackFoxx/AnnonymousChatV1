package toyproject.annonymouschat.chat.repository;

import toyproject.annonymouschat.chat.model.Chat;

public interface CustomChatRepository {
    Chat getRandom(Long userId);
}
