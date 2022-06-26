package toyproject.annonymouschat.chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import toyproject.annonymouschat.User.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/*
Chat 테이블과 동일한 구조
*/
@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id @GeneratedValue
    private Long id;

    private String content;

    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private Timestamp createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}
