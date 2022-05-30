package toyproject.annonymouschat.User.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Setter
@Getter
public class UserRegistrationDto {
    @NotNull
    @NotEmpty
    @Email
    @NotBlank
    private String userEmail;

    @Size(min = 4, max = 20)
    @NotEmpty
    @NotBlank
    private String password;
}
