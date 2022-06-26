package toyproject.annonymouschat.replychat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyInfo {
    private Date createDate;
    private String chatContent;
    private Date chatCreateDate;
}
