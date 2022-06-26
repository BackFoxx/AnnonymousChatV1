package toyproject.annonymouschat.replychat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;

import java.util.List;

public interface ReplyChatRepository extends JpaRepository<ReplyChat, Long> {
    List<ReplyChat> findAllByChatId(Long chatId);
    List<ReplyChat> findAllByUserId(Long userId);

    @Query("select new toyproject.annonymouschat.replychat.dto.ReplyInfo(r.createDate, r.chat.content, r.chat.createDate) from ReplyChat r")
    ReplyInfo replyInfo(Long replyId);
}
