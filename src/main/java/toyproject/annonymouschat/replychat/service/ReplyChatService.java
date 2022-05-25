package toyproject.annonymouschat.replychat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepositoryImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyChatService {
    private final ReplyChatRepository repository;

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
