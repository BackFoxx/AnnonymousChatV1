package toyproject.annonymouschat.User.repository;

import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.User.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public String registration(User user) {
        em.persist(user);
        log.info("회원가입 완료되었습니다.");

        return user.getUserEmail();
    }
}
