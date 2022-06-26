package toyproject.annonymouschat.replychat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.chat.model.Chat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyChat {
    @Id @GeneratedValue
    private Long id;
    private String content;

    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "CHAT_ID")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
