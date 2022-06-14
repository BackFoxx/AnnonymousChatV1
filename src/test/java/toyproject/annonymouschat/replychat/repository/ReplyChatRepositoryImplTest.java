package toyproject.annonymouschat.replychat.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;
import toyproject.annonymouschat.replychat.dto.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReplyChatRepositoryImplTest {

    ReplyChatRepository replyChatRepository;
    UserRepository userRepository;
    ChatRepository chatRepository;

    @BeforeEach
    void setup(@Autowired ApplicationContext applicationContext) {
        this.replyChatRepository = applicationContext.getBean(ReplyChatRepository.class);
        this.userRepository = applicationContext.getBean(UserRepository.class);
        this.chatRepository = applicationContext.getBean(ChatRepository.class);
    }

    @Test
    @DisplayName("reply 저장_성공")
    @Transactional
    void saveReply_success() {
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);

        this.saveReply(user, chatId);

        RepliesByUserIdDto repliesByUserIdDto = new RepliesByUserIdDto();
        repliesByUserIdDto.setUserId(user.getId());
        RepliesByUserIdResponseDto savedReply = this.replyChatRepository.findAllByUserIdDto(repliesByUserIdDto).get(0);

        assertThat(savedReply.getReplyContent()).isEqualTo("I'm Reply");
    }

    @Test
    @DisplayName("Chat Id로 모든 reply 찾기_ 성공")
    @Transactional
    void findAllByChatId_success() {
        User user = this.getUser("test@gmail.com");
        Long AChatId = this.getChatId(user);
        Long BChatId = this.getChatId(user);
        this.saveReply(user, AChatId);
        this.saveReply(user, AChatId);
        this.saveReply(user, BChatId);
        // A에 대한 reply 2개와 B에 대한 reply 1개

        List<RepliesByChatIdResponseDto> findReplyList = this.replyChatRepository.findAllByChatIdDto(AChatId);
        assertThat(findReplyList).hasSize(2);
    }

    @Test
    @DisplayName("Chat Id로 모든 reply 찾기_ 성공 (0개)")
    @Transactional
    void findAllByChatId_success_0() {
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);
        // 0개 등록

        List<RepliesByChatIdResponseDto> findReplyList = this.replyChatRepository.findAllByChatIdDto(chatId);
        assertThat(findReplyList).isNotNull(); // 값이 없으면 null이 아닌 빈 배열이 나온다.
        assertThat(findReplyList).hasSize(0);
    }

    @Test
    @DisplayName("User Id로 모든 reply 찾기_성공")
    @Transactional
    void findAllByUserID() {
        User user = this.getUser("test@gmail.com");
        User user2 = this.getUser("test2@gmail.com");
        Long chatId = this.getChatId(user);

        this.saveReply(user, chatId);
        this.saveReply(user, chatId);
        this.saveReply(user2, chatId);
        /* 내가 쓴 글 2개와 타인이 쓴 글 1개 */

        RepliesByUserIdDto repliesByUserIdDto = new RepliesByUserIdDto();
        repliesByUserIdDto.setUserId(user.getId());
        List<RepliesByUserIdResponseDto> findReplyDto = this.replyChatRepository.findAllByUserIdDto(repliesByUserIdDto);

        assertThat(findReplyDto).hasSize(2);
    }

    @Test
    @DisplayName("User Id로 모든 reply 찾기_성공 (0개)")
    @Transactional
    void findAllByUserID_success_0() {
        // case 1. 존재하지 않는 유저 Id로 조회
        RepliesByUserIdDto repliesByUserIdDto = new RepliesByUserIdDto();
        repliesByUserIdDto.setUserId(-1L); //존재하지 않는 유저
        List<RepliesByUserIdResponseDto> findReplyDto
                = this.replyChatRepository.findAllByUserIdDto(repliesByUserIdDto);

        assertThat(findReplyDto).hasSize(0);

        // case 2. 작성한 글이 없는 유저 Id로 조회
        User user = this.getUser("test@gmail.com");

        RepliesByUserIdDto repliesByUserIdDto2 = new RepliesByUserIdDto();
        repliesByUserIdDto2.setUserId(user.getId());
        List<RepliesByUserIdResponseDto> findReplyDto2
                = this.replyChatRepository.findAllByUserIdDto(repliesByUserIdDto2);

        assertThat(findReplyDto2).hasSize(0);
    }

    @Test
    @DisplayName("reply 삭제_성공")
    @Transactional
    void deleteReply_success() {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);

        this.saveReply(user, chatId);

        RepliesByUserIdDto userIdDto = new RepliesByUserIdDto();
        userIdDto.setUserId(user.getId());
        List<RepliesByUserIdResponseDto> findReplyList
                = this.replyChatRepository.findAllByUserIdDto(userIdDto);

        assertThat(findReplyList).hasSize(1);
        /* reply가 잘 등록되었는지 확인 */

        // when
        Long replyId = findReplyList.get(0).getReplyId();

        ReplyDeleteDto deleteDto = new ReplyDeleteDto();
        deleteDto.setReplyId(replyId);
        this.replyChatRepository.deleteReply(deleteDto);

        // then
        findReplyList = this.replyChatRepository.findAllByUserIdDto(userIdDto);
        assertThat(findReplyList).hasSize(0);
        /* reply가 잘 삭제되었는지 확인 */
    }

    @Test
    @DisplayName("reply 정보 가져오기_성공")
    @Transactional
    void replyInfo_success(@Autowired ChatRepository chatRepository) {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);
        Chat chat = chatRepository.findByChatId(chatId);

        this.saveReply(user, chatId);

        RepliesByUserIdDto userIdDto = new RepliesByUserIdDto();
        userIdDto.setUserId(user.getId());
        List<RepliesByUserIdResponseDto> findReplyList
                = this.replyChatRepository.findAllByUserIdDto(userIdDto);

        // when
        Long replyId = findReplyList.get(0).getReplyId();
        ReplyInfo replyInfo = this.replyChatRepository.replyInfo(replyId);

        // then
        assertThat(replyInfo.getChatCreateDate()).isEqualTo(chat.getCreateDate());
        assertThat(replyInfo.getChatContent()).isEqualTo(chat.getContent());
    }

    private void saveReply(User user, Long chatId) {
        ReplyChatSaveDto saveDto = new ReplyChatSaveDto();
        saveDto.setUserId(user.getId());
        saveDto.setChatId(chatId);
        saveDto.setContent("I'm Reply");

        this.replyChatRepository.saveReply(saveDto);
    }

    private User getUser(String userEmail) {
        UserRegistrationDto meDto = new UserRegistrationDto();
        meDto.setUserEmail(userEmail);
        meDto.setPassword("test");

        this.userRepository.registration(meDto);
        return this.userRepository.findByUserEmail(userEmail);
    }

    private Long getChatId(User user) {
        ChatSaveDto chatSaveDto = new ChatSaveDto();
        chatSaveDto.setUserId(user.getId());
        chatSaveDto.setContent("I'm random one");
        return this.chatRepository.save(chatSaveDto);
    }
}