package toyproject.annonymouschat.replychat.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class ReplyChat {
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp createDate;
    private Long ChatId;
    private Long UserId;
}
