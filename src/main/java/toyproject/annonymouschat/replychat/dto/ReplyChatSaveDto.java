package toyproject.annonymouschat.replychat.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ReplyChatSaveDto {
    @NotEmpty
    @Length(max = 140)
    private String content;

    @NotNull(message = "이미 삭제되었거나 존재하지 않는 Chat입니다.")
    private Long chatId;
    private Long userId;
}
