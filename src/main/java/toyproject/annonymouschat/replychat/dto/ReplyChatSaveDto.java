package toyproject.annonymouschat.replychat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReplyChatSaveDto {
    private String content;
    private Long chatId;
    private Long userId;
}
