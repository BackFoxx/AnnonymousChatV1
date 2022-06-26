package toyproject.annonymouschat.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.annonymouschat.chat.model.Chat;

import java.util.List;

public interface ChatRepository
        extends JpaRepository<Chat, Long>, CustomChatRepository {
    List<Chat> findAllByUserId(Long userId);
    void deleteById(Long id);
}
