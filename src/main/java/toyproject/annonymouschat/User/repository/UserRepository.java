package toyproject.annonymouschat.User.repository;

import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;

public interface UserRepository {
    public String registration(UserRegistrationDto dto);
    public User findByUserEmail(String userEmail);
}
