package toyproject.annonymouschat.User.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistrationDto {
    private String userEmail;
    private String password;
}
