package toyproject.annonymouschat.User.repository;

import toyproject.annonymouschat.User.model.User;

public interface UserRepository {
    String registration(User user);
    User findByUserEmail(String userEmail);
}
