package toyproject.annonymouschat.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.annonymouschat.User.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    User findByUserEmail(String userEmail);
}
