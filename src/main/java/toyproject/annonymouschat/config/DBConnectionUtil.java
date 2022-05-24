package toyproject.annonymouschat.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static toyproject.annonymouschat.config.ConnectionConst.*;
/*
ConnectionConst의 정보를 바탕으로
커넥션을 반환합니다.

SQLException은 체크 예외이므로
RuntimeException으로 변환하여 던집니다.
*/
@Slf4j
public class DBConnectionUtil {
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(URL);
            hikariDataSource.setUsername(USERNAME);
            hikariDataSource.setPassword(PASSWORD);
            hikariDataSource.setMaximumPoolSize(10);
            dataSource = hikariDataSource;
        }
        return dataSource;
    }
}
