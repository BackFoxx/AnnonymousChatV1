package toyproject.annonymouschat.chat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ChatServiceTest {
    @Autowired
    ChatService chatService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Chat 저장_성공")
    void save_success() {

        User user = new User(null, "test@gmail.com", "password");
        this.userRepository.save(user);

        ChatSaveDto chatSaveDto = new ChatSaveDto();
        chatSaveDto.setUserId(user.getId());
        chatSaveDto.setContent("Hi service");

        Chat saveChat = this.chatService.save(chatSaveDto);

        assertThat(saveChat.getId()).isNotNull();
        assertThat(saveChat.getContent()).isEqualTo("Hi service");
    }

}