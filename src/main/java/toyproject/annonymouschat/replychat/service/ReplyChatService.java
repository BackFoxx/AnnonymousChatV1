package toyproject.annonymouschat.replychat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyChatService {
    private final ReplyChatRepository repository;

    public void saveReply(ReplyChatSaveDto dto) {
        repository.saveReply(dto);
    }
    public List<RepliesByChatIdResponseDto> findAllByChatId(Long chatId) {
        return repository.findAllByChatIdDto(chatId);
    }
    public List<RepliesByUserIdResponseDto> findAllByUserId(RepliesByUserIdDto dto) {
        return repository.findAllByUserIdDto(dto);
    }

    public void deleteReply(ReplyDeleteDto dto) {
        repository.deleteReply(dto);
    }

    public ReplyInfo replyInfo(Long replyId) {
        return repository.replyInfo(replyId);
    }
}
