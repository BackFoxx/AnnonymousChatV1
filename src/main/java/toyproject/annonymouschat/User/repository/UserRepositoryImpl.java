package toyproject.annonymouschat.User.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;

import javax.sql.DataSource;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;
//    private DataSource dataSource = DBConnectionUtil.getDataSource();
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 아이디를 이용한 삭제
    @Override
    public String registration(UserRegistrationDto dto) {
        String sql = "insert into user_table (useremail, password) values (?, ?)";

        jdbcTemplate.update(sql, dto.getUserEmail(), dto.getPassword());
        log.info("회원가입 완료되었습니다.");

        return dto.getUserEmail();
    }

    @Override
    public User findByUserEmail(String userEmail) {
        String sql = "select * from user_table where useremail=?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(rs.getLong("id"),
                    rs.getString("userEmail"),
                    rs.getString("password")), userEmail);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
