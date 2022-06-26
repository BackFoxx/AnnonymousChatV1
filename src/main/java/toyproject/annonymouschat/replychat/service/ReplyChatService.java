package toyproject.annonymouschat.replychat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.repository.ChatRepository;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;
import toyproject.annonymouschat.replychat.repository.ReplyChatRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyChatService {
    private final ReplyChatRepository replyChatRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public void saveReply(ReplyChatSaveDto dto) {
        ReplyChat replyChat = ReplyChat.builder()
                .user(userRepository.findById(dto.getUserId()).get())
                .chat(chatRepository.findById(dto.getChatId()).get())
                .content(dto.getContent())
                .build();

        replyChatRepository.save(replyChat);
    }

    public List<RepliesByChatIdResponseDto> findAllByChatId(Long chatId) {
        List<ReplyChat> replyList = replyChatRepository.findAllByChatId(chatId);

        List<RepliesByChatIdResponseDto> responseList = new ArrayList<>();
        replyList.forEach(reply -> {
            RepliesByChatIdResponseDto dto = new RepliesByChatIdResponseDto();
            dto.setCreateDate(reply.getCreateDate());
            dto.setContent(reply.getContent());
            responseList.add(dto);
        });

        return responseList;
    }

    public List<RepliesByUserIdResponseDto> findAllByUserId(RepliesByUserIdDto userIdDto) {
        List<ReplyChat> findList = replyChatRepository.findAllByUserId(userIdDto.getUserId());

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

    @Transactional
    public void deleteReply(ReplyDeleteDto dto) {

        replyChatRepository.deleteById(dto.getReplyId());
    }

    public ReplyInfo replyInfo(Long replyId) {
        return replyChatRepository.replyInfo(replyId);
    }
}
