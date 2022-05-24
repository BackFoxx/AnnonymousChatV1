package toyproject.annonymouschat.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static toyproject.annonymouschat.config.ConnectionConst.*;

@Slf4j
class DBConnectionUtilTest {

//    @Test
//    @DisplayName("getConnection성공케이스")
//    void getConnection_성공케이스() {
//        Connection connection = DBConnectionUtil.getConnection();
//        Connection connection2 = DBConnectionUtil.getConnection();
//        Assertions.assertThat(connection).isNotNull();
//        log.info("커넥션 드라이버 : {}, class : {}", connection, connection.getClass());
//        log.info("커넥션 드라이버 : {}, class : {}", connection2, connection2.getClass());
//    }

//    @Test
//    @DisplayName("스프링이 제공하는 DriverManager")
//    void dataSourceDriverManager() throws SQLException {
//        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//        Connection connection = dataSource.getConnection();
//        Connection connection2 = dataSource.getConnection();
//        log.info("connection : {}, class : {}", connection, connection.getClass());
//        log.info("connection : {}, class : {}", connection2, connection2.getClass());
//    }

    @Test
    @DisplayName("HikaCP 커넥션 풀")
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        Connection connection = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        Thread.sleep(1000L);
        log.info("connection : {}, class : {}", connection, connection.getClass());
        log.info("connection : {}, class : {}", connection2, connection2.getClass());
    }
}