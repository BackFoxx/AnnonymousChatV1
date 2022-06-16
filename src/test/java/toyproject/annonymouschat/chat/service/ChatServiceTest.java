package toyproject.annonymouschat.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    ChatService chatService;

    @Mock
    ChatRepository chatRepository;

    @BeforeEach
    void setup() {
        chatService = new ChatService(this.chatRepository);
    }

    @Test
    @DisplayName("Chat 저장_성공")
    void save_success() {
        Chat chat = new Chat(110L,
                "Hi service",
                Timestamp.valueOf(LocalDateTime.now()),
                1L);

        Chat dtoToChat = new Chat(null,
                "Hi service",
                null,
                1L);

        when(this.chatRepository.save(dtoToChat)).thenReturn(110L);
        when(this.chatRepository.findByChatId(110L)).thenReturn(chat);

        ChatSaveDto chatSaveDto = new ChatSaveDto();
        chatSaveDto.setUserId(1L);
        chatSaveDto.setContent("Hi service");

        Chat saveChat = this.chatService.save(chatSaveDto);
        assertThat(saveChat).isEqualTo(chat);
    }

}