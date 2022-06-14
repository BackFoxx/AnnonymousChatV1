package toyproject.annonymouschat.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatPostSaveDeleteResponseDto {
    private boolean ok;
    private String message;
}
