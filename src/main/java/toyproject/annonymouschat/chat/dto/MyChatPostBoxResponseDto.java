package toyproject.annonymouschat.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class MyChatPostBoxResponseDto {
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp createDate;
}
