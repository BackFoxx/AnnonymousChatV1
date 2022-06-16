package toyproject.annonymouschat.replychat.repository;

import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;

import java.util.List;

public interface ReplyChatRepository {
    void saveReply(ReplyChat replyChat);

    List<ReplyChat> findAllByChatIdDto(Long chatId);

    List<ReplyChat> findAllByUserIdDto(Long userId);

    void deleteReply(Long replyId);

    ReplyInfo replyInfo(Long replyId);
}
