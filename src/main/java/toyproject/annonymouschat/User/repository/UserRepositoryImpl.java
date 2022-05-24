package toyproject.annonymouschat.User.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.config.DBConnectionUtil;

import javax.sql.DataSource;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private DataSource dataSource = DBConnectionUtil.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 아이디를 이용한 삭제
    @Override
    public String registration(UserRegistrationDto dto) {
        String sql = "insert into user (useremail, password) values (?, ?)";

        jdbcTemplate.update(sql, dto.getUserEmail(), dto.getPassword());
        log.info("회원가입 완료되었습니다.");

        return dto.getUserEmail();
    }

    @Override
    public User findByUserEmail(String userEmail) {
        String sql = "select * from user where useremail=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(rs.getLong("id"),
                rs.getString("userEmail"),
                rs.getString("password")), userEmail);
    }

}
