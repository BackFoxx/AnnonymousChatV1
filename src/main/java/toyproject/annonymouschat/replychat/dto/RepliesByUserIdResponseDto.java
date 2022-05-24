package toyproject.annonymouschat.replychat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class RepliesByUserIdResponseDto {

    private String chatContent;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp chatCreateDate;

    private Long replyId;
    private String replyContent;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp replyCreateDate;
}
