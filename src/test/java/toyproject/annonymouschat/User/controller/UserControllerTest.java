package toyproject.annonymouschat.User.controller;

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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;

import javax.servlet.http.Cookie;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserControllerTest {

    @Autowired
    ApplicationContext applicationContext;

    ObjectMapper objectMapper;
    UserController userController;
    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.objectMapper = this.applicationContext.getBean(ObjectMapper.class);
        this.userController = this.applicationContext.getBean(UserController.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("????????????_??????")
    void registration_success() throws Exception {
        // given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUserEmail("test@gmail.com");
        registrationDto.setPassword("test");

        // when
        mockMvc.perform(post("/login/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value(true))
                .andExpect(jsonPath("message").value("???????????? ?????????????????????."))

                .andDo(document("user-registration",
                        requestFields(
                                fieldWithPath("userEmail").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("password").description("??????????????? ?????????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("???????????? ?????? ?????? ????????? boolean????????? ???????????????."),
                                fieldWithPath("message").description("???????????? ?????? ????????? ?????? ?????? ??????????????????.")
                        )));
    }

    @Test
    @DisplayName("????????????_?????? (?????? ????????? ?????????)")
    void registration_fail_duplicatedEmail(@Autowired UserRepository userRepository) throws Exception {
        // given
        User user = new User(null, "test@gmail.com", "password");
        userRepository.registration(user);

        UserRegistrationDto duplicatedUserRegistrationDto = new UserRegistrationDto();
        duplicatedUserRegistrationDto.setUserEmail("test@gmail.com"); // ?????? ????????? ?????????
        duplicatedUserRegistrationDto.setPassword("anotherPassword");

        // when
        mockMvc.perform(post("/login/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatedUserRegistrationDto)))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("ok").value(false))
                .andExpect(jsonPath("message").value("?????? ???????????? ???????????? ??????????????????."));
    }

    private static Object[] paramsForRegistration_fail_validation() {
        return new Object[]{
                new Object[]{"testGmailCom", "test", "????????? ????????? ??????????????????!"},
                new Object[]{"", "test", "????????? ?????? ??????????????? ????????? ????????? ?????????"},
                new Object[]{"test@gmail.com", "t", "4 ~ 20??? ????????? ????????? ?????????!"},
        };
    }

    @ParameterizedTest(name = "{displayName} - {2}")
    @MethodSource("paramsForRegistration_fail_validation")
    @DisplayName("????????????_?????? (?????? ?????? ??????)")
    void registration_fail_validation(String userEmail, String password, String errorMessage) throws Exception {
        // given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUserEmail(userEmail); // Email
        registrationDto.setPassword(password);

        // when
        mockMvc.perform(post("/login/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("?????????_??????")
    void login_success(@Autowired UserRepository userRepository) throws Exception {
        // given
        String userEmail = "test@gmail.com";
        String password = "test";

        User user = new User(null, userEmail, password);

        userRepository.registration(user);

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserEmail(userEmail);
        userLoginDto.setPassword(password);

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SessionId"))
                .andExpect(content().string("??????"))

                .andDo(document("user-login",
                        requestFields(
                                fieldWithPath("userEmail").description("???????????? ????????? ???????????????."),
                                fieldWithPath("password").description("???????????? ?????????????????????.")
                        )
                        ));
    }

    private static Object[] paramsForLogin_fail_validation() {
        return new Object[]{
                new Object[]{"", "password", "????????? ?????? ??????????????? ????????? ????????? ?????????"},
                new Object[]{"test@gmail.com", "", "???????????? ?????? ??????????????? ????????? ????????? ?????????"}
        };
    }

    @ParameterizedTest(name = "{displayName} - {2}")
    @MethodSource("paramsForLogin_fail_validation")
    @DisplayName("?????????_?????? (?????? ??????)")
    void login_fail_validation(String userEmail, String password, String errorMessage) throws Exception {
        // given
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserEmail(userEmail);
        userLoginDto.setPassword(password);

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("????????????_??????")
    void logout_Success() throws Exception {
        //given & when
        mockMvc.perform(get("/logout")
                        .cookie(new Cookie("SessionId", "00A0-A000-0A00-00A0")))

                // then
                .andDo(print())
                .andExpect(cookie().maxAge("SessionId", 0))

                .andDo(document("user-logout"));
    }
}