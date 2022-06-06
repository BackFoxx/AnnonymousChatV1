package toyproject.annonymouschat.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ChatSaveDto {
    private Long UserId;

    @Length(max = 140)
    @NotEmpty
    private String content;
}
