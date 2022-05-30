package toyproject.annonymouschat.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ChatSaveDto {
    @NotNull(message = "로그인 상태에 문제가 있습니다")
    private Long UserId;

    @Max(value = 140, message = "최대 140자까지 입력 가능합니다")
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
}
