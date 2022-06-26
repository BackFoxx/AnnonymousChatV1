package toyproject.annonymouschat.chat.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.model.Chat;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChatRepositoryImplTest {

    ChatRepository chatRepository;
    UserRepository userRepository;
    EntityManager em;

    @BeforeEach
    void setup(ApplicationContext applicationContext) {
        chatRepository = applicationContext.getBean(ChatRepository.class);
        userRepository = applicationContext.getBean(UserRepository.class);
        em = applicationContext.getBean(EntityManager.class);
    }

    @Test
    @DisplayName("게시글 저장_성공")
    @Transactional
    void save_success() {
        User user = this.getUser("test@gmail.com");

        Chat chat = new Chat(null, "What's happening on here!", Timestamp.from(Instant.now()), user);

        Chat saveChat = this.chatRepository.save(chat);
        System.out.println("saveChat = " + saveChat);

        Long saveId = saveChat.getId();
        assertThat(saveId).isNotNull();
    }

    @Test
    @DisplayName("유저 아이디 이용한 전체조회_성공")
    @Transactional
    void findAllByUserId_success() {
        // given

        /* 내가 쓴 Chat과 */
        User user = this.getUser("test@gmail.com");

        Chat chat = Chat.builder()
                .user(user)
                .content("written by test@gmail.com")
                .build();

        Long saveChatId = this.chatRepository.save(chat).getId();

        /* 남이 쓴 Chat */
        User user2 = this.getUser("test2@gmail.com");

        Chat chat2 = Chat.builder()
                .user(user2)
                .content("written by test2@gmail.com")
                .build();

        this.chatRepository.save(chat2);

        // when
        List<Chat> findList = this.chatRepository.findAllByUserId(user.getId());

        // then
        findList.forEach(findChat -> {
            assertThat(findChat.getId()).isEqualTo(saveChatId);
        });
    }

    @Test
    @DisplayName("Chat 아이디를 이용한 단건조회_성공")
    @Transactional
    void findByChatId_success() {
        // given
        User user = this.getUser("test@gmail.com");

        Chat chat = Chat.builder()
                .user(user)
                .content("What's happening on here!")
                .build();

        Long saveChatId = this.chatRepository.save(chat).getId();

        // when
        Chat findChat = this.chatRepository.findById(saveChatId).get();

        // then
        assertThat(findChat.getId()).isEqualTo(saveChatId);
        assertThat(findChat.getUser().getId()).isEqualTo(user.getId());
        assertThat(findChat.getContent()).isEqualTo("What's happening on here!");
    }

    @Test
    @DisplayName("Chat 아이디를 이용한 단건조회_실패 (없음)")
    @Transactional
    void findByChatId_fail() {
        Assertions.assertFalse(this.chatRepository.findById(-1L).isPresent());
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_성공")
    @Transactional
    void getRandom_success() {
        // given
        User user = this.getUser("test@gmail.com");

        IntStream.range(0, 15).forEach(i -> {
            Chat chat = Chat.builder()
                    .user(user)
                    .content("What's happening on here!")
                    .build();

            this.chatRepository.save(chat);
        });

        User user2 = this.getUser("test2@gmail.com");
        IntStream.range(0, 15).forEach(i -> {
            Chat chat = Chat.builder()
                    .user(user2)
                    .content("What's happening on here!")
                    .build();

             this.chatRepository.save(chat);
        });

        // when
        Chat randomChat = this.chatRepository.getRandom(user.getId());
        Chat randomChat2 = this.chatRepository.getRandom(user.getId());
        while (randomChat.getId() == randomChat2.getId()) {
            randomChat = this.chatRepository.getRandom(user.getId());
            randomChat2 = this.chatRepository.getRandom(user.getId());
        }

        // then
        assertThat(randomChat.getUser().getId()).isNotEqualTo(user.getId());
        assertThat(randomChat2.getUser().getId()).isNotEqualTo(user.getId());
        // A가 랜덤한 Chat을 찾을 때 A가 쓴 글을 빼고 찾아내는지 테스트

        assertThat(randomChat.getId()).isNotEqualTo(randomChat2.getId());
        // Chat이 랜덤하게 출력되는지 테스트 (같은 Chat이 나올 수도 있지만 한 번이라도 위 테스트가 성공하면 랜덤으로 출력되는 것이 맞음)
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_실패 (조회하는 사람이 로그인 안 되어있음)")
    @Transactional
    void getRandom_fail_not_login() {
        Assertions.assertNull(this.chatRepository.getRandom(null));
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_실패 (조회할 Chat이 없음)")
    @Transactional
    void getRandom_fail_not_found() {
        User user = this.getUser("test@gmail.com");

        Chat chat = Chat.builder()
                .user(user)
                .content("What's happening on here!")
                .build();

        this.chatRepository.save(chat);
        // 내가 쓴 Chat은 있는데 남이 쓴 Chat은 없음

        Assertions.assertNull(this.chatRepository.getRandom(user.getId()));
    }

    @Test
    @DisplayName("삭제_성공")
    @Transactional
    void delete_success() {
        // given
        User user = this.getUser("test@gmail.com");

        Chat chat = Chat.builder()
                .user(user)
                .content("What's happening on here!")
                .build();

        Long saveId = this.chatRepository.save(chat).getId();
        assertThat(this.chatRepository.findById(saveId).get()).isNotNull();

        // when
        this.chatRepository.delete(chat);

        // then
        Assertions.assertFalse(this.chatRepository.findById(saveId).isPresent());
    }

    private User getUser(String userEmail) {
        User user = new User(null, userEmail, "test");
        this.userRepository.registration(user);

        return user;
    }
}