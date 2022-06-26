package toyproject.annonymouschat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.chat.dto.ChatDeleteDto;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.chat.repository.ChatRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public Chat save(ChatSaveDto chatSaveDto) {
        Chat chat = new Chat();
        chat.setContent(chatSaveDto.getContent());
        chat.setCreateDate(Timestamp.from(Instant.now()));
        User user = userRepository.findById(chatSaveDto.getUserId()).get();
        chat.setUser(user);

        return chatRepository.save(chat);
    }

    @Transactional
    public void delete(ChatDeleteDto chatDeleteDto) {
        Chat chat = chatRepository.findById(chatDeleteDto.getId()).get();
        chatRepository.delete(chat);
    }

    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId) {
        List<Chat> findChatList = chatRepository.findAllByUserId(userId);

        List<MyChatPostBoxResponseDto> responseList = new ArrayList<>();
        findChatList.forEach(chat -> {
            MyChatPostBoxResponseDto dto = new MyChatPostBoxResponseDto();
            dto.setId(chat.getId());
            dto.setContent(chat.getContent());
            dto.setCreateDate(chat.getCreateDate());

            responseList.add(dto);
        });

        return responseList;
    }

    public Chat getRandom(Long userId) {
        return chatRepository.getRandom(userId);
    }
    public Chat findByChatId(Long id) {
        return chatRepository.findById(id).get();
    }
}
