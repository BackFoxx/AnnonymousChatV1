package toyproject.annonymouschat.replychat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.repository.ChatRepository;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
class ReplyControllerTest {
    @Autowired
    ApplicationContext applicationContext;

    MockMvc mockMvc;
    ReplyController replyController;
    ObjectMapper objectMapper;

    UserRepository userRepository;
    ReplyChatRepository replyChatRepository;
    ChatRepository chatRepository;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.replyController = this.applicationContext.getBean(ReplyController.class);
        this.objectMapper = this.applicationContext.getBean(ObjectMapper.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).build();

        this.userRepository = this.applicationContext.getBean(UserRepository.class);
        this.replyChatRepository = this.applicationContext.getBean(ReplyChatRepository.class);
        this.chatRepository = this.applicationContext.getBean(ChatRepository.class);
    }

    @Test
    @DisplayName("Chat Id로 reply 조회하기_성공")
    @Transactional
    void repliesByChatId_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);

        this.saveReply(user, chatId);
        this.saveReply(user, chatId);
        this.saveReply(user, chatId);
        /* 3개 저장 */

        // when
        mockMvc.perform(get("/reply/replies/{chatId}", chatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("Chat Id로 reply 조회하기_성공 (0개)")
    @Transactional
    void repliesByChatId_success_0() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);

        // when
        mockMvc.perform(get("/reply/replies/{chatId}", chatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("내 reply 보기_성공")
    @Transactional
    void myReply_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        User user2 = this.getUser("test2@gmail.com");

        Long chatId = this.getChatId(user2);

        this.saveReply(user, chatId);
        this.saveReply(user2, chatId);
        this.saveReply(user2, chatId);

        // when
        mockMvc.perform(get("/reply/my-reply")
                        .requestAttr("user", user))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("내 reply 보기_성공 (0개)")
    @Transactional
    void myReply_success_0() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");

        // when
        mockMvc.perform(get("/reply/my-reply")
                        .requestAttr("user", user))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("내 reply 보기_실패 (로그인 안되어있음)")
    @Transactional
    void myReply_fail_unlogin() throws Exception {
        mockMvc.perform(get("/reply/my-reply"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertThat(result.getResponse().getErrorMessage())
                            .isEqualTo("Missing request attribute 'user' of type User");
                });
    }

    @Test
    @DisplayName("reply id로 reply 정보 가져오기_성공")
    @Transactional
    void replyInfo_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);
        this.saveReply(user, chatId);

        RepliesByUserIdDto userIdDto = new RepliesByUserIdDto();
        userIdDto.setUserId(user.getId());
        RepliesByUserIdResponseDto findReply = this.replyChatRepository.findAllByUserIdDto(userIdDto).get(0);
        Long replyId = findReply.getReplyId();

        // when
        mockMvc.perform(get("/reply/my-reply/info/{reply_id}", replyId)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("chatContent").exists())
                .andExpect(jsonPath("chatCreateDate").exists());
    }

    @Test
    @DisplayName("reply id로 reply 정보 가져오기_실패 (해당하는 reply 없음)")
    @Transactional
    void replyInfo_success_fail_notfound() throws Exception {
        assertThatThrownBy(() -> {
            mockMvc.perform(get("/reply/my-reply/info/{reply_id}", -1)
                            /* 존재하지 않는 reply 아이디*/
                            .accept(MediaType.APPLICATION_JSON))

                    .andDo(print())
                    .andExpect(status().isOk());
        }).isInstanceOf(NestedServletException.class)
                .hasRootCauseInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("reply 삭제_성공")
    @Transactional
    void deleteReply_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);
        this.saveReply(user, chatId);

        RepliesByUserIdDto userIdDto = new RepliesByUserIdDto();
        userIdDto.setUserId(user.getId());

        List<RepliesByUserIdResponseDto> findReplyList = this.replyChatRepository.findAllByUserIdDto(userIdDto);
        assertThat(findReplyList).hasSize(1);
        /* reply가 정상적으로 저장되었는지 확인 */

        RepliesByUserIdResponseDto findReply = findReplyList.get(0);
        Long replyId = findReply.getReplyId();

        // when
        ReplyDeleteDto replyDeleteDto = new ReplyDeleteDto();
        replyDeleteDto.setReplyId(replyId);

        mockMvc.perform(post("/reply/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(replyDeleteDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("삭제 완료되었습니다"));

        List<RepliesByUserIdResponseDto> deletedReplyList = this.replyChatRepository.findAllByUserIdDto(userIdDto);
        assertThat(deletedReplyList).hasSize(0);
        /* reply가 정상적으로 삭제되었는지 확인 */
    }

    @Test
    @DisplayName("reply 저장_성공")
    @Transactional
    void saveReply_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Long chatId = this.getChatId(user);

        ReplyChatSaveDto chatSaveDto = new ReplyChatSaveDto();
        chatSaveDto.setUserId(user.getId());
        chatSaveDto.setChatId(chatId);
        chatSaveDto.setContent("I'm here");

        // when
        mockMvc.perform(post("/reply/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr("user", user)
                        .content(this.objectMapper.writeValueAsString(chatSaveDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("저장 완료되었습니다"));

        List<RepliesByChatIdResponseDto> replyChatList = this.replyChatRepository.findAllByChatIdDto(chatId);
        assertThat(replyChatList).hasSize(1);
        /* 저장이 잘 되어있는지 확인 */
    }

    private static Object[] paramsForSaveReplyFailValidation() {
        return new Object[]{
                new Object[]{"", 1L, "내용을 입력해주세요!"},
                new Object[]{"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 1L, "140자 이내로 작성해 주세요!"},
                new Object[]{"안녕하세요", null, "이미 삭제되었거나 존재하지 않는 Chat입니다."},
        };
    }

    @ParameterizedTest(name = "{displayName} - {2}")
    @DisplayName("reply 저장_실패 (검증 오류)")
    @MethodSource("paramsForSaveReplyFailValidation")
    @Transactional
    void saveReply_fail_validation(String content, Long chatId, String errorMessage) throws Exception {
        // given
        User user = this.getUser("test@gmail.com");

        ReplyChatSaveDto chatSaveDto = new ReplyChatSaveDto();
        chatSaveDto.setUserId(user.getId());
        chatSaveDto.setChatId(chatId);
        chatSaveDto.setContent(content);

        // when
        mockMvc.perform(post("/reply/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr("user", user)
                        .content(this.objectMapper.writeValueAsString(chatSaveDto)))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }


    @Test
    @DisplayName("reply 저장_실패 (로그인 안되어 있음)")
    @Transactional
    void saveReply_fail_unLogin() throws Exception {
        ReplyChatSaveDto chatSaveDto = new ReplyChatSaveDto();

        // when
        mockMvc.perform(post("/reply/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        /* 유저 request Attribute 없음 */
                        .content(this.objectMapper.writeValueAsString(chatSaveDto)))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertThat(result.getResponse().getErrorMessage())
                            .isEqualTo("Missing request attribute 'user' of type User");
                });
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