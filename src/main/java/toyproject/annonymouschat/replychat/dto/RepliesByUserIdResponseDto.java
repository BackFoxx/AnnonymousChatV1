package toyproject.annonymouschat.replychat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class RepliesByUserIdResponseDto {
    private Long replyId;
    private String replyContent;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Date replyCreateDate;
}
