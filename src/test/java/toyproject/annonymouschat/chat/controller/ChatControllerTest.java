package toyproject.annonymouschat.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.WebApplicationContext;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatDeleteDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class ChatControllerTest {

    @Autowired
    ApplicationContext applicationContext;

    MockMvc mockMvc;
    ChatController chatController;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.chatController = this.applicationContext.getBean(ChatController.class);
        this.objectMapper = this.applicationContext.getBean(ObjectMapper.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                ).build();
    }

    @Test
    @DisplayName("랜덤 chat 가져오기_성공")
    @Transactional
    void getRandom_success(@Autowired UserRepository userRepository,
                           @Autowired ChatRepository chatRepository) throws Exception {
        // given

        // 조회하는 유저의 정보
        User me = this.getUser("test@gmail.com", userRepository);

        // 다른 유저가 작성한 글
        User anotherUser = this.getUser("test2@gmail.com", userRepository);
        this.saveChat(chatRepository, anotherUser);

        // when
        this.mockMvc.perform(get("/chat/postbox")
                        .requestAttr("user", me)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("userId").doesNotExist())

                .andDo(document("chat-getRandom",
                        responseFields(
                                fieldWithPath("id").description("가져온 Chat의 아이디입니다."),
                                fieldWithPath("content").description("가져온 Chat의 내용입니다."),
                                fieldWithPath("createDate").description("가져온 Chat의 작성일입니다."),
                                fieldWithPath("userId").description("가져온 Chat을 작성한 사람의 Id입니다. 사용자가 알 수 없도록 null로 지정되어 있습니다.")
                        )
                ));
    }

    @Test
    @DisplayName("랜덤 chat 가져오기_실패 (결과가 없음)")
    @Transactional
    void getRandom_fail_notfound(@Autowired UserRepository userRepository) throws Exception {
        // given

        // 조회하는 유저의 정보
        User me = this.getUser("test@gmail.com", userRepository);

        // when
        // 타인이 쓴 글이 없으므로 responseBody가 비어있음
        this.mockMvc.perform(get("/chat/postbox")
                        .requestAttr("user", me)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("랜덤 chat 가져오기_실패 (로그인이 안되어 있음)")
    @Transactional
    /* Request Attribute의 user 값에 null이 들어가는 경우 */
    void getRandom_fail_unLogin() throws Exception {
        assertThatThrownBy(() -> {
            this.mockMvc.perform(get("/chat/postbox")
                    .requestAttr("user", null)
                    .accept(MediaType.APPLICATION_JSON));
        }).isInstanceOf(IllegalArgumentException.class);
    } // 로그인이 안되어 있으면 filter에서 "user" 값에 null을 넣어줄 수가 없으므로 mockMvc 자체가 작동이 안된다.

    @Test
    @DisplayName("랜덤 chat 가져오기_실패 (로그인이 안되어 있음2)")
    @Transactional
    /* Request Attribute의 user 깂 자체가 없는 경우 */
    void getRandom_fail_unLogin2() throws Exception {
        // when
        this.mockMvc.perform(get("/chat/postbox")
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(ServletRequestBindingException.class));
                /* 헤더값을 찾지 못했으므로 400 에러 발생*/
    }

    @Test
    @DisplayName("chat 삭제_성공")
    @Transactional
    void delete_success(@Autowired UserRepository userRepository,
                        @Autowired ChatRepository chatRepository) throws Exception {
        // given
        User user = this.getUser("test@gmail.com", userRepository);
        this.saveChat(chatRepository, user);

        Long chatId = chatRepository
                .findAllByUserId(user.getId())
                .get(0)
                .getId();

        /* chat이 정상적으로 저장되었음을 확인한다. */
        assertThat(chatRepository.findByChatId(chatId)).isNotNull();

        ChatDeleteDto deleteDto = new ChatDeleteDto();
        deleteDto.setId(chatId);


        // when
        this.mockMvc.perform(post("/chat/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(this.objectMapper.writeValueAsString(deleteDto)))

                // then
                .andDo(print())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("삭제 완료되었습니다"))

                .andDo(document("chat-delete",
                        requestFields(
                                fieldWithPath("id").description("삭제할 Chat의 id입니다.")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("정상적으로 삭제되었는지 여부를 boolean으로 표시합니다."),
                                fieldWithPath("message").description("삭제 완료 메시지 또는 에러 메시지입니다.")
                        )
                ));

        /* chat이 정상적으로 삭제되었음을 확인한다. */
        assertThatThrownBy(() -> chatRepository.findByChatId(chatId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("chat 저장_성공")
    @Transactional
    void save_success(@Autowired UserRepository userRepository) throws Exception {
        // given
        User user = this.getUser("test@gmail.com", userRepository);

        ChatSaveDto saveDto = new ChatSaveDto();
        saveDto.setUserId(user.getId());
        saveDto.setContent("I'm good");

        // when
        mockMvc.perform(post("/chat/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .requestAttr("user", user)
                        .content(this.objectMapper.writeValueAsString(saveDto)))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("저장 완료되었습니다."))

                .andDo(document("chat-save",
                        requestFields(
                                fieldWithPath("content").description("저장할 chat의 내용입니다."),
                                fieldWithPath("userId").description("chat을 저장할 user의 id입니다. 요청 바디에 직접 넣을 수는 없고, 필터에서 유저 정보를 찾아 주입해줍니다.")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("chat의 저장 여부를 boolean값으로 알려줍니다."),
                                fieldWithPath("message").description("저장 완료 메시지 또는 에러 메시지입니다.")
                        )));
    }

    private static Object[] paramsForSaveFailValidation() {
        return new Object[]{
                new Object[]{"", "내용을 입력해주세요!"},
                new Object[]{"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "140자 이내로 작성해 주세요!"}
        };
    }

    @ParameterizedTest(name = "{displayName} - {1}")
    @DisplayName("chat 저장_실패 (검증 에러)")
    @MethodSource("paramsForSaveFailValidation")
    @Transactional
    void save_fail_validation(String content, String errorMessage, @Autowired UserRepository userRepository) throws Exception {
        // given
        User user = this.getUser("test@gmail.com", userRepository);

        ChatSaveDto saveDto = new ChatSaveDto();
        saveDto.setUserId(user.getId());
        saveDto.setContent(content);

        // when
        mockMvc.perform(post("/chat/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr("user", user)
                        .content(this.objectMapper.writeValueAsString(saveDto)))

                //then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("내 chat 모두 가져오기_성공")
    @Transactional
    void myChat_success(@Autowired UserRepository userRepository,
                        @Autowired ChatRepository chatRepository) throws Exception {
        // given
        User user = this.getUser("test@gmail.com", userRepository);
        this.saveChat(chatRepository, user);
        this.saveChat(chatRepository, user);
        this.saveChat(chatRepository, user);
        /* 저장된 게시글 3개 */

        User user2 = this.getUser("test2@gmail.com", userRepository);
        this.saveChat(chatRepository, user2);
        /* 다른 유저로 저장된 게시글 1개 */

        // when
        MvcResult result = mockMvc.perform(get("/chat/myChat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .cookie(new Cookie("SessionId", "A000-0A00-00A0-000A"))
                        .requestAttr("user", user))

                // then
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("chat-myChat",
                        responseFields(
                                fieldWithPath("[].id").description("찾아낸 chat의 id입니다."),
                                fieldWithPath("[].content").description("찾아낸 chat의 내용입니다."),
                                fieldWithPath("[].createDate").description("찾아낸 chat의 작성일입니다.")
                        )
                        ))

                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        MyChatPostBoxResponseDto[] findChatsList
                = this.objectMapper.readValue(contentAsString, MyChatPostBoxResponseDto[].class);

        assertThat(findChatsList).hasSize(3);
        /* user로 저장한 게시글이 3개이므로 찾아낸 리스트 크기도 3이어야 한다. */
    }

    @Test
    @DisplayName("내 chat 모두 가져오기_성공 (가져올 chat 없음)")
    @Transactional
    void myChat_success_notfound(@Autowired UserRepository userRepository,
                        @Autowired ChatRepository chatRepository) throws Exception {
        // given
        User user = this.getUser("test@gmail.com", userRepository);
        /* 저장된 게시글 0개 */

        User user2 = this.getUser("test2@gmail.com", userRepository);
        this.saveChat(chatRepository, user2);
        /* 다른 유저로 저장된 게시글 1개 */

        // when
        MvcResult result = mockMvc.perform(get("/chat/myChat")
                        .requestAttr("user", user))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        MyChatPostBoxResponseDto[] findChatsList
                = this.objectMapper.readValue(contentAsString, MyChatPostBoxResponseDto[].class);

        assertThat(findChatsList).hasSize(0);
        /* user로 저장한 게시글이 0개이므로 찾아낸 리스트 크기도 0이어야 한다. */
    }

    @Test
    @DisplayName("내 chat 모두 가져오기_실패 (로그인 안되어 있음)")
    @Transactional
    void myChat_fail_unLogin() throws Exception {
        mockMvc.perform(get("/chat/myChat"))
                /* request attribute에 유저 정보 없음 */
                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(ServletRequestBindingException.class));
    }

    private User getUser(String userEmail, UserRepository userRepository) {
        User user = new User(null, userEmail, "test");

        userRepository.registration(user);
        return userRepository.findByUserEmail(userEmail);
    }

    private void saveChat(ChatRepository chatRepository, User anotherUser) {
        Chat chat = Chat.builder()
                .userId(anotherUser.getId())
                .content("I'm random one")
                .build();

        chatRepository.save(chat);
    }
}