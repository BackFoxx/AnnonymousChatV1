package toyproject.annonymouschat.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    private String userEmail;
    private String password;
}
