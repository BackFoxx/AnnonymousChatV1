package toyproject.annonymouschat.replychat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import toyproject.annonymouschat.replychat.dto.*;
import toyproject.annonymouschat.config.DBConnectionUtil;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
public class ReplyChatRepositoryImpl implements ReplyChatRepository {

    private DataSource dataSource = DBConnectionUtil.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    @Override
    public void saveReply(ReplyChatSaveDto dto) {
        String sql = "insert into replychat (content, chatId, user_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, dto.getContent(), dto.getChatId(), dto.getUserId());
    }

    @Override
    public List<RepliesByChatIdResponseDto> findAllByChatIdDto(RepliesByChatIdDto dto) {
        String sql = "select * from replychat where chatid = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RepliesByChatIdResponseDto responseDto = new RepliesByChatIdResponseDto();
            responseDto.setContent(rs.getString("content"));
            responseDto.setCreateDate(rs.getTimestamp("createDate"));
            return responseDto;
        }, dto.getChatId());
    }

    @Override
    public List<RepliesByUserIdResponseDto> findAllByUserIdDto(RepliesByUserIdDto dto) {
        String sql = "select C.CREATEDATE CHAT_CREATEDATE, C.CONTENT CHAT_CONTENT, RC.ID REPLY_ID, RC.CREATEDATE REPLY_CREATEDATE, RC.CONTENT REPLY_CONTENT from REPLYCHAT RC inner join CHAT C on C.ID = RC.CHATID where RC.USER_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RepliesByUserIdResponseDto responseDto = new RepliesByUserIdResponseDto();
            responseDto.setChatContent(rs.getString("CHAT_CONTENT"));
            responseDto.setChatCreateDate(rs.getTimestamp("CHAT_CREATEDATE"));

            responseDto.setReplyId(rs.getLong("REPLY_ID"));
            responseDto.setReplyContent(rs.getString("REPLY_CONTENT"));
            responseDto.setReplyCreateDate(rs.getTimestamp("REPLY_CREATEDATE"));

            return responseDto;
        }, dto.getUserId());
    }

    @Override
    public void deleteReply(ReplyDeleteDto dto) {
        String sql = "delete from replychat where id = ?";
        jdbcTemplate.update(sql, dto.getReplyId());
        log.info("삭제 완료되었습니다. 삭제한 replyId = {}", dto.getReplyId());
    }
}
