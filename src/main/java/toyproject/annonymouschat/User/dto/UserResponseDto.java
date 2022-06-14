package toyproject.annonymouschat.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private boolean ok;
    private String message;
}
