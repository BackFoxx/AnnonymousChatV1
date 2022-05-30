package toyproject.annonymouschat.replychat.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ReplyChatSaveDto {
    @NotNull(message = "Not null")
    @NotEmpty(message = "내용을 입력해주세요")
    @Max(value = 140, message = "최대 {1}자까지 입력 가능합니다")
    private String content;

    @NotNull(message = "잘못된 게시글 아이디")
    private Long chatId;

    @NotNull(message = "로그인에 문제가 있는 것 같습니다")
    private Long userId;
}
