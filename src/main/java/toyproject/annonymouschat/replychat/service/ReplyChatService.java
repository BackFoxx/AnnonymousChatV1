package toyproject.annonymouschat.replychat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyChatService {
    private final ReplyChatRepository repository;

    public void saveReply(ReplyChatSaveDto dto) {

        ReplyChat replyChat = ReplyChat.builder()
                .userId(dto.getUserId())
                .chatId(dto.getChatId())
                .content(dto.getContent())
                .build();

        repository.saveReply(replyChat);
    }
    public List<RepliesByChatIdResponseDto> findAllByChatId(Long chatId) {
        List<ReplyChat> replyList = repository.findAllByChatIdDto(chatId);

        List<RepliesByChatIdResponseDto> responseList = new ArrayList<>();
        replyList.forEach(reply -> {
            RepliesByChatIdResponseDto dto = new RepliesByChatIdResponseDto();
            dto.setCreateDate(dto.getCreateDate());
            dto.setContent(dto.getContent());
            responseList.add(dto);
        });

        return responseList;
    }
    public List<RepliesByUserIdResponseDto> findAllByUserId(RepliesByUserIdDto userIdDto) {
        List<ReplyChat> findList = repository.findAllByUserIdDto(userIdDto.getUserId());

        List<RepliesByUserIdResponseDto> responseList = new ArrayList<>();
        findList.forEach(reply -> {
            RepliesByUserIdResponseDto dto = new RepliesByUserIdResponseDto();
            dto.setReplyId(reply.getId());
            dto.setReplyContent(reply.getContent());
            dto.setReplyCreateDate(reply.getCreateDate());

            responseList.add(dto);
        });

        return responseList;
    }

    public void deleteReply(ReplyDeleteDto dto) {
        repository.deleteReply(dto.getReplyId());
    }

    public ReplyInfo replyInfo(Long replyId) {
        return repository.replyInfo(replyId);
    }
}
