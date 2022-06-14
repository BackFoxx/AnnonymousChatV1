package toyproject.annonymouschat.User.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class UserLoginDto {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String password;
}
