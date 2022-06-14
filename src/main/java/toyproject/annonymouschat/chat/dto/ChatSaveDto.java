package toyproject.annonymouschat.chat.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChatSaveDto {
    private Long UserId;

    @Length(max = 140)
    @NotEmpty
    private String content;
}
