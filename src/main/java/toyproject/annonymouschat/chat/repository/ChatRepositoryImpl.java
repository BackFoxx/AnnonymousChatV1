package toyproject.annonymouschat.chat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.*;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;
import toyproject.annonymouschat.config.DBConnectionUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Slf4j
public class ChatRepositoryImpl implements ChatRepository{

    private DataSource dataSource = DBConnectionUtil.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 게시글 저장
    @Override
    public Long save(ChatSaveDto chatSaveDto) {
        String sql = "insert into chat(content, user_id) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, chatSaveDto.getContent());
            ps.setLong(2, chatSaveDto.getUserId());
            return ps;
        }, keyHolder);

        log.info("Generated id = {}", keyHolder.getKeys().get("ID"));
        return (Long) keyHolder.getKeys().get("ID");
    }

    @Override
    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId) {
        String sql = "select * from chat where user_id=?";

        List<MyChatPostBoxResponseDto> findChats = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MyChatPostBoxResponseDto dto = new MyChatPostBoxResponseDto();
            dto.setId(rs.getLong("id"));
            dto.setContent(rs.getString("content"));
            dto.setCreateDate(rs.getTimestamp("createDate"));
            return dto;
        }, userId);

        return findChats;
    }

    //Chat 아이디를 이용한 단건조회
    @Override
    public Chat findByChatId(Long id) {
        String sql = "select * from chat where id=?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Chat chat = new Chat();
            chat.setId(rs.getLong("id"));
            chat.setContent(rs.getString("content"));
            chat.setCreateDate(rs.getTimestamp("createDate"));
            return chat;
        }, id);

    }

    @Override
    public Chat getRandom(Long userId) {
        String sql = "select * from chat where not user_id in (?) order by rand() limit 1";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Chat chat = new Chat();
            chat.setId(rs.getLong("id"));
            chat.setContent(rs.getString("content"));
            chat.setCreateDate(rs.getTimestamp("createDate"));
            return chat;
        }, userId);
    }

    // 아이디를 이용한 삭제
    @Override
    public void delete(Long id) {
        String sql = "delete from chat where id=?";
        jdbcTemplate.update(sql, id);
    }

}
