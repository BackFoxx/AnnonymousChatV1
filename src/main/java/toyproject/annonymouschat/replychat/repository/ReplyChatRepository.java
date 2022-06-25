package toyproject.annonymouschat.replychat.repository;

import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;

import java.util.List;

public interface ReplyChatRepository {
    void saveReply(ReplyChat replyChat);

    List<ReplyChat> findAllByChatId(Long chatId);

    List<ReplyChat> findAllByUserId(Long userId);

    void deleteReply(Long replyId);

    ReplyInfo replyInfo(Long replyId);
}
