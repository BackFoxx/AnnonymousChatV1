package toyproject.annonymouschat.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class UserLoginDto {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String password;
}
