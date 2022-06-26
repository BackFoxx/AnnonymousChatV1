package toyproject.annonymouschat.chat.dto;

import lombok.Builder;
import lombok.Getter;
import toyproject.annonymouschat.chat.model.Chat;

import java.sql.Timestamp;
@Getter
@Builder
public class GetRandomResponseDto {
    private Long id;
    private String content;
    private Timestamp createDate;

    public static GetRandomResponseDto toDto(Chat chat) {
        return GetRandomResponseDto.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .createDate(chat.getCreateDate())
                .build();
    }
}
