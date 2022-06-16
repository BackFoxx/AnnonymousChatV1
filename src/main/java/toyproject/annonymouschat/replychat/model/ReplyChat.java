package toyproject.annonymouschat.replychat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class ReplyChat {
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp createDate;
    private Long chatId;
    private Long userId;
}
