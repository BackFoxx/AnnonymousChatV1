package toyproject.annonymouschat.replychat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplyChatSaveDeleteResponseDto {
    private boolean ok;
    private String message;
}
