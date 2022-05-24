package toyproject.annonymouschat.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatSaveDto {
    private Long UserId;
    private String content;
}
