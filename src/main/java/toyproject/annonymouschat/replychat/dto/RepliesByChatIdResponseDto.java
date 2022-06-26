package toyproject.annonymouschat.replychat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepliesByChatIdResponseDto {
    private String content;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Date createDate;
}
