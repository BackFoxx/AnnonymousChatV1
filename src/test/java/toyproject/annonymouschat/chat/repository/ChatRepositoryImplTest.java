package toyproject.annonymouschat.chat.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChatRepositoryImplTest {

    ChatRepository chatRepository;
    UserRepository userRepository;

    @BeforeEach
    void setup(ApplicationContext applicationContext) {
        chatRepository = applicationContext.getBean(ChatRepository.class);
        userRepository = applicationContext.getBean(UserRepository.class);
    }

    @Test
    @DisplayName("게시글 저장_성공")
    @Transactional
    void save_success() {
        Long userId = this.getUserId("test@gmail.com");

        ChatSaveDto chatSaveDto = new ChatSaveDto();
        chatSaveDto.setUserId(userId);
        chatSaveDto.setContent("What's happening on here!");

        Long saveId = this.chatRepository.save(chatSaveDto);
        assertThat(saveId).isNotNull();
    }

    @Test
    @DisplayName("유저 아이디 이용한 전체조회_성공")
    @Transactional
    void findAllByUserId_success() {
        // given
        Long userId = this.getUserId("test@gmail.com");
        Long userId2 = this.getUserId("test2@gmail.com");

        ChatSaveDto dtoBy1 = new ChatSaveDto();
        dtoBy1.setUserId(userId);
        dtoBy1.setContent("written by test@gmail.com");

        Long saveChatId = this.chatRepository.save(dtoBy1);

        ChatSaveDto dtoBy2 = new ChatSaveDto();
        dtoBy2.setUserId(userId2);
        dtoBy2.setContent("written by test2@gmail.com");

        // when
        List<MyChatPostBoxResponseDto> findList = this.chatRepository.findAllByUserId(userId);

        // then
        findList.forEach(chat -> {
            assertThat(chat.getId()).isEqualTo(saveChatId);
        });
    }

    @Test
    @DisplayName("Chat 아이디를 이용한 단건조회_성공")
    @Transactional
    void findByChatId_success() {
        // given
        Long userId = this.getUserId("test@gmail.com");

        ChatSaveDto saveDto = new ChatSaveDto();
        saveDto.setUserId(userId);
        saveDto.setContent("What's happening on here!");

        Long saveChatId = this.chatRepository.save(saveDto);

        // when
        Chat findChat = this.chatRepository.findByChatId(saveChatId);

        // then
        assertThat(findChat.getId()).isEqualTo(saveChatId);
        assertThat(findChat.getUserId()).isEqualTo(userId);
        assertThat(findChat.getContent()).isEqualTo("What's happening on here!");
    }

    @Test
    @DisplayName("Chat 아이디를 이용한 단건조회_실패 (없음)")
    @Transactional
    void findByChatId_fail() {
        assertThatThrownBy(() -> this.chatRepository.findByChatId(-1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_성공")
    @Transactional
    void getRandom_success() {
        // given
        Long userId = getUserId("test@gmail.com");
        IntStream.range(0, 15).forEach(i -> {
            ChatSaveDto saveDto = new ChatSaveDto();
            saveDto.setUserId(userId);
            saveDto.setContent("What's happening on here!");

            this.chatRepository.save(saveDto);
        });

        Long userId2 = getUserId("test2@gmail.com");
        IntStream.range(0, 15).forEach(i -> {
            ChatSaveDto saveDto = new ChatSaveDto();
            saveDto.setUserId(userId2);
            saveDto.setContent("What's happening on here!");

            this.chatRepository.save(saveDto);
        });

        // when
        Chat randomChat = this.chatRepository.getRandom(userId);
        Chat randomChat2 = this.chatRepository.getRandom(userId);
        while (randomChat.getId() == randomChat2.getId()) {
            randomChat = this.chatRepository.getRandom(userId);
            randomChat2 = this.chatRepository.getRandom(userId);
        }

        // then
        assertThat(randomChat.getUserId()).isNotEqualTo(userId);
        assertThat(randomChat2.getUserId()).isNotEqualTo(userId);
        // A가 랜덤한 Chat을 찾을 때 A가 쓴 글을 빼고 찾아내는지 테스트

        assertThat(randomChat.getId()).isNotEqualTo(randomChat2.getId());
        // Chat이 랜덤하게 출력되는지 테스트 (같은 Chat이 나올 수도 있지만 한 번이라도 위 테스트가 성공하면 랜덤으로 출력되는 것이 맞음)
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_실패 (조회하는 사람이 로그인 안 되어있음)")
    @Transactional
    void getRandom_fail_not_login() {
        assertThatThrownBy(() -> this.chatRepository.getRandom(null))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("랜덤하게 Chat 가져오기_실패 (조회할 Chat이 없음)")
    @Transactional
    void getRandom_fail_not_found() {
        Long userId = this.getUserId("test@gmail.com");

        ChatSaveDto saveDto = new ChatSaveDto();
        saveDto.setUserId(userId);
        saveDto.setContent("What's happening on here!");

        this.chatRepository.save(saveDto);
        // 내가 쓴 Chat은 있는데 남이 쓴 Chat은 없음

        assertThatThrownBy(() -> this.chatRepository.getRandom(userId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("삭제_성공")
    @Transactional
    void delete_success() {
        // given
        Long userId = this.getUserId("test@gmail.com");

        ChatSaveDto saveDto = new ChatSaveDto();
        saveDto.setUserId(userId);
        saveDto.setContent("What's happening on here!");

        Long saveId = this.chatRepository.save(saveDto);
        assertThat(this.chatRepository.findByChatId(saveId)).isNotNull();

        // when
        this.chatRepository.delete(saveId);

        // then
        assertThatThrownBy(() -> this.chatRepository.findByChatId(saveId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private Long getUserId(String userEmail) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUserEmail(userEmail);
        registrationDto.setPassword("test");

        this.userRepository.registration(registrationDto);
        User findUser = this.userRepository.findByUserEmail(userEmail);
        return findUser.getId();
    }
}