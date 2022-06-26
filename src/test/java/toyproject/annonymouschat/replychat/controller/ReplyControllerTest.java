package toyproject.annonymouschat.replychat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;
import toyproject.annonymouschat.replychat.dto.ReplyChatSaveDto;
import toyproject.annonymouschat.replychat.dto.ReplyDeleteDto;
import toyproject.annonymouschat.replychat.model.ReplyChat;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                ).build();

        this.userRepository = this.applicationContext.getBean(UserRepository.class);
        this.replyChatRepository = this.applicationContext.getBean(ReplyChatRepository.class);
        this.chatRepository = this.applicationContext.getBean(ChatRepository.class);
    }

    @Test
    @DisplayName("Chat Id로 reply 조회하기_성공")
    @Transactional
    void getAllReplyByChatId_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Chat chat = this.getChat(user);

        this.saveReply(user, chat);
        this.saveReply(user, chat);
        this.saveReply(user, chat);
        /* 3개 저장 */

        // when
        mockMvc.perform(get("/reply/replies/{chatId}", chat.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andDo(document("reply-getAllReplyByChatId",
                        responseFields(
                                fieldWithPath("[].content").description("찾아낸 reply의 내용입니다."),
                                fieldWithPath("[].createDate").description("찾아낸 reply의 작성일입니다.")
                        )));
    }

    @Test
    @DisplayName("Chat Id로 reply 조회하기_성공 (0개)")
    @Transactional
    void getAllReplyByChatId_success_0() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Chat chat = this.getChat(user);

        // when
        mockMvc.perform(get("/reply/replies/{chatId}", chat.getId())
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

        Chat chat = this.getChat(user2);

        this.saveReply(user, chat);
        this.saveReply(user2, chat);
        this.saveReply(user2, chat);

        // when
        mockMvc.perform(get("/reply/my-reply")
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr("user", user))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))

                .andDo(document("reply-myReply",
                        responseFields(
                                fieldWithPath("[].replyId").description("내 reply의 id입니다."),
                                fieldWithPath("[].replyContent").description("내 reply의 내용입니다."),
                                fieldWithPath("[].replyCreateDate").description("내 reply의 작성일입니다.")
                        )));
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
        Chat chat = this.getChat(user);
        this.saveReply(user, chat);

        ReplyChat findReply = this.replyChatRepository.findAllByUserId(user.getId()).get(0);
        Long replyId = findReply.getId();

        // when
        mockMvc.perform(get("/reply/my-reply/info/{reply_id}", replyId)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("chatContent").exists())
                .andExpect(jsonPath("chatCreateDate").exists())

                .andDo(document("reply-replyInfo",
                        responseFields(
                                fieldWithPath("createDate").description("내 reply의 작성일입니다."),
                                fieldWithPath("chatContent").description("내가 답을 단 Chat의 내용입니다."),
                                fieldWithPath("chatCreateDate").description("내가 답을 단 Chat의 작성일입니다.")
                        )));
    }

    @Test
    @DisplayName("reply id로 reply 정보 가져오기_실패 (해당하는 reply 없음)")
    @Transactional
    void replyInfo_success_fail_notfound() throws Exception {
        mockMvc.perform(get("/reply/my-reply/info/{reply_id}", -1)
                        /* 존재하지 않는 reply 아이디*/
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("reply 삭제_성공")
    @Transactional
    void deleteReply_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Chat chat = this.getChat(user);
        this.saveReply(user, chat);

        List<ReplyChat> findReplyList = this.replyChatRepository.findAllByUserId(user.getId());
        assertThat(findReplyList).hasSize(1);
        /* reply가 정상적으로 저장되었는지 확인 */

        ReplyChat findReply = findReplyList.get(0);
        Long replyId = findReply.getId();

        // when
        ReplyDeleteDto replyDeleteDto = new ReplyDeleteDto();
        replyDeleteDto.setReplyId(replyId);

        mockMvc.perform(post("/reply/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(this.objectMapper.writeValueAsString(replyDeleteDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("삭제 완료되었습니다"))

                .andDo(document("reply-deleteReply",
                        requestFields(
                                fieldWithPath("replyId").description("삭제할 reply의 id입니다.")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("삭제가 정상적으로 수행되었는지 여부를 boolean 값으로 나타냅니다."),
                                fieldWithPath("message").description("삭제 성공 메시지 또는 에러 메시지입니다.")
                        )));

        List<ReplyChat> deletedReplyList = this.replyChatRepository.findAllByUserId(user.getId());
        assertThat(deletedReplyList).hasSize(0);
        /* reply가 정상적으로 삭제되었는지 확인 */
    }

    @Test
    @DisplayName("reply 저장_성공")
    @Transactional
    void saveReply_success() throws Exception {
        // given
        User user = this.getUser("test@gmail.com");
        Chat chat = this.getChat(user);

        ReplyChatSaveDto chatSaveDto = new ReplyChatSaveDto();
//        chatSaveDto.setUserId(user.getId());
        chatSaveDto.setChatId(chat.getId());
        chatSaveDto.setContent("I'm here");

        // when
        mockMvc.perform(post("/reply/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .requestAttr("user", user)
                        .content(this.objectMapper.writeValueAsString(chatSaveDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("저장 완료되었습니다"))

                .andDo(document("reply-saveReply",
                        requestFields(
                                fieldWithPath("content").description("저장할 reply의 내용입니다."),
                                fieldWithPath("chatId").description("어떤 chat에 대한 reply인지 id를 나타냅니다."),
                                fieldWithPath("userId").description("reply를 저장하는 유저의 id입니다. 전송되는 쿠키 값을 분석하여 서버 내에서 유저 id를 찾습니다.")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("저장이 정상적으로 수행되었는지 여부를 boolean 값으로 나타냅니다."),
                                fieldWithPath("message").description("저장 성공 메시지 또는 에러 메시지입니다.")
                        )));

        List<ReplyChat> replyChatList = this.replyChatRepository.findAllByChatId(chat.getId());
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

    private void saveReply(User user, Chat chat) {
        ReplyChat replyChat = ReplyChat.builder()
                .createDate(Date.from(Instant.now()))
                .user(user)
                .chat(chat)
                .content("I'm Reply")
                .build();

        this.replyChatRepository.save(replyChat);
    }

    private User getUser(String userEmail) {
        User user = new User(null, userEmail, "test");

        this.userRepository.registration(user);
        return this.userRepository.findByUserEmail(userEmail);
    }

    private Chat getChat(User user) {
        Chat chat = Chat.builder()
                .content("I'm random one")
                .createDate(Timestamp.from(Instant.now()))
                .user(user)
                .build();
        return this.chatRepository.save(chat);
    }
}