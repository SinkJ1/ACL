package sinkj1.security.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/*@Configuration
@ConfigurationProperties(prefix = "params.datasource")*/
public class JpaConfig extends HikariConfig {

/*
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws SQLException {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setSchema(TenantContext.getTenantId());
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        DataSource dataSource = hikariDataSource;
        return dataSource;
    }
*/
}
