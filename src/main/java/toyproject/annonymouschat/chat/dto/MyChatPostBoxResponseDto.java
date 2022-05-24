package toyproject.annonymouschat.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class MyChatPostBoxResponseDto {
    private Long id;
    private String content;
    private Timestamp createDate;
}
