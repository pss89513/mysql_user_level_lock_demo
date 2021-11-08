package woowa.demo;

import com.zaxxer.hikari.HikariDataSource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import woowa.demo.lock.UserLevelLockFinal;
import woowa.demo.lock.UserLevelLockWithJdbcTemplate;
import woowa.demo.lock.UserLevelLockWithRedis;

import javax.sql.DataSource;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties("userlock.datasource.hikari")
    public HikariDataSource userLockDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.baseline();
            flyway.migrate();
        };
    }

    @Bean
    public UserLevelLockWithJdbcTemplate userLevelLockWithJdbcTemplate() {
        return new UserLevelLockWithJdbcTemplate(new NamedParameterJdbcTemplate(userLockDataSource()));
    }

    @Bean
    public UserLevelLockFinal userLevelLockFinal() {
        return new UserLevelLockFinal(userLockDataSource());
    }
}
