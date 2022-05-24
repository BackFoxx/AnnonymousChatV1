package toyproject.annonymouschat.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChatPostSaveDeleteResponseDto {
    private boolean ok;
    private String message;
}
