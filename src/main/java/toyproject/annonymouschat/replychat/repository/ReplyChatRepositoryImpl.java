package toyproject.annonymouschat.replychat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.replychat.model.ReplyChat;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class ReplyChatRepositoryImpl implements ReplyChatRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public ReplyChatRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveReply(ReplyChat replyChat) {
        String sql = "insert into replychat (content, chat_id, user_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, replyChat.getContent(), replyChat.getChatId(), replyChat.getUserId());
    }

    @Override
    public List<ReplyChat> findAllByChatIdDto(Long chatId) {
        String sql = "select * from replychat where chat_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> ReplyChat.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .createDate(rs.getTimestamp("createDate"))
                .chatId(rs.getLong("chat_id"))
                .userId(rs.getLong("user_id"))
                .build(), chatId);
    }

    @Override
    public List<ReplyChat> findAllByUserIdDto(Long userId) {
        String sql = "select * from REPLYCHAT where USER_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> ReplyChat.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .createDate(rs.getTimestamp("createDate"))
                .chatId(rs.getLong("chat_id"))
                .userId(rs.getLong("user_id"))
                .build(), userId);
    }

    @Override
    public void deleteReply(Long replyId) {
        String sql = "delete from replychat where id = ?";
        jdbcTemplate.update(sql, replyId);
        log.info("삭제 완료되었습니다. 삭제한 replyId = {}", replyId);
    }

    @Override
    public ReplyInfo replyInfo(Long replyId) {
        String sql = "select REPLYCHAT.CREATEDATE create_date, C.CONTENT chat_content, C.CREATEDATE chat_createdate from REPLYCHAT inner join CHAT C on C.ID = REPLYCHAT.CHAT_ID where REPLYCHAT.ID = ?";
        List<ReplyInfo> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReplyInfo replyInfo = new ReplyInfo();
            replyInfo.setCreateDate(rs.getTimestamp("create_date"));
            replyInfo.setChatContent(rs.getString("chat_content"));
            replyInfo.setChatCreateDate(rs.getTimestamp("chat_createdate"));
            return replyInfo;
        }, replyId);
        return result.get(0);
    }
}
