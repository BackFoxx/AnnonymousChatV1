package toyproject.annonymouschat.chat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import toyproject.annonymouschat.chat.dto.ChatSaveDto;
import toyproject.annonymouschat.chat.dto.MyChatPostBoxResponseDto;
import toyproject.annonymouschat.chat.model.Chat;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
public class ChatRepositoryImpl implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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

    //User 아이디를 이용한 전체조회 -> 날짜 최신순
    @Override
    public List<MyChatPostBoxResponseDto> findAllByUserId(Long userId) {
        String sql = "select * from chat where user_id=? order by createDate desc";

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
            chat.setUserId(rs.getLong("user_id"));
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
            // 글 쓴 사람을 추측할 수 없도록 user_id를 dto에 넘겨주지 않는다.
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
