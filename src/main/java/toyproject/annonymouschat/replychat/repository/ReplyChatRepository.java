package toyproject.annonymouschat.replychat.repository;

import toyproject.annonymouschat.replychat.dto.*;

import java.util.List;

public interface ReplyChatRepository {
    public void saveReply(ReplyChatSaveDto dto);

    public List<RepliesByChatIdResponseDto> findAllByChatIdDto(int chatId);

    public List<RepliesByUserIdResponseDto> findAllByUserIdDto(RepliesByUserIdDto dto);

    public void deleteReply(ReplyDeleteDto dto);

    public ReplyInfo replyInfo(int replyId);
}
