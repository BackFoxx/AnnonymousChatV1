package toyproject.annonymouschat.chat.repository;

import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.chat.model.Chat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Random;

@Slf4j
public class CustomChatRepositoryImpl implements CustomChatRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Chat getRandom(Long userId) {
        long count = (long) em.createQuery("select count(c) from Chat c where c.user.id <> :userId")
                .setParameter("userId", userId)
                .getSingleResult();

        if (count == 0) {
            log.info("조회된 chat 없음");
            return null;
        }

        Random random = new Random();
        int number = random.nextInt((int) count);

        return em.createQuery("select c from Chat c where c.user.id <> :userId", Chat.class)
                .setParameter("userId", userId)
                .setFirstResult(number)
                .setMaxResults(1)
                .getSingleResult();
    }
}
