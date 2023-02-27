package org.prgrms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
class CustomerJDBCRepositoryTest {

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )
    static class Config{

//        .type(HikariDataSource):HikariDataSource 로 DataSource 를 만듦.
        @Bean
        public DataSource dataSource(){
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("test1234@#")
                    .type(HikariDataSource.class)
                    .build();
//            아무 것도 지정하지 않으면 10개가 minimum 이자 maximum
//            dataSource.setMinimumIdle(100); // connection pool 의 최소 connection 개수. 100개를 기본적으로 만들고 꺼내 쓰겠다.
//            dataSource.setMaximumPoolSize(1000); // connection pool 의 최대 connection 개수
            return dataSource;
        }
    }

    @Autowired
    CustomerJDBCRepository customerJDBCRepository;

    @Autowired
    DataSource dataSource;

    @Test
    public void testHikariConnetionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() throws InterruptedException {
        var customers = customerJDBCRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
        /*
        Thread.sleep(15000);
        show status like '%Threads%';

         */
    }
}