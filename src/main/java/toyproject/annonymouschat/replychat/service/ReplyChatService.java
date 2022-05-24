package toyproject.annonymouschat.replychat.service;

import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepositoryImpl;

import java.util.List;

public class ReplyChatService {
    private ReplyChatRepository repository = new ReplyChatRepositoryImpl();

    public void saveReply(ReplyChatSaveDto dto) {
        repository.saveReply(dto);
    }
    public List<RepliesByChatIdResponseDto> findAllByChatId(RepliesByChatIdDto dto) {
        return repository.findAllByChatIdDto(dto);
    }
    public List<RepliesByUserIdResponseDto> findAllByUserId(RepliesByUserIdDto dto) {
        return repository.findAllByUserIdDto(dto);
    }

    public void deleteReply(ReplyDeleteDto dto) {
        repository.deleteReply(dto);
    }
}
