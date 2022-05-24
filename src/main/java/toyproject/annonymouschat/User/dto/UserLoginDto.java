package toyproject.annonymouschat.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserLoginDto {
    private String userEmail;
    private String password;
}
