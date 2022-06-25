package toyproject.annonymouschat.restdoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class RestDocControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("Rest 문서 생성 테스트")
    void restApiDocument() throws Exception {
        //given
        RestDocDto dto = RestDocDto.builder()
                .name("테스트")
                .age(50)
                .location("성남시")
                .build();

        //when
        this.mockMvc.perform(get("/rest-doc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("테스트 뭄무"))
                .andExpect(jsonPath("age").value(150))
                .andExpect(jsonPath("location").value("성남시 뭄무"))

                // document
                .andDo(document("index",
                        requestFields(
                                fieldWithPath("name").description("Dto의 이름입니다."),
                                fieldWithPath("age").description("Dto의 나이입니다."),
                                fieldWithPath("location").description("Dto의 거주 지역입니다.")
                        ),
                        responseFields(
                                fieldWithPath("name").description("Dto의 이름에 뭄무가 붙었습니다."),
                                fieldWithPath("age").description("Dto의 나이에 100이 늘어났습니다."),
                                fieldWithPath("location").description("Dto의 거주 지역에 뭄무가 붙었습니다.")
                        )));
    }
}