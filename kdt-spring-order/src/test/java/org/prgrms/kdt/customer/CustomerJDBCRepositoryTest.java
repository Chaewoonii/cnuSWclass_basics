package org.prgrms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //Order annotation 의 순서로 실행하겠다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // test instance lifecycle은 클래스 당 하나임을 지정.
                                                // -> 지정하지 않으면 clean을 할 때 static이어야 하는데(전부 지워야 하므로) 클래스당 하나로 지정하면 static 일 필요가 없음.
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

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
    }

//    @Autowired
//    CustomerJDBCRepository customerJdbcRepository;



    @Autowired
    CustomerJDBCTemplateRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    //테스트 시작 전 딱 한 번 실행
    @BeforeAll
    void setUp(){
        newCustomer = new Customer(UUID.randomUUID(), "testtest-user", "testtest-user@gmail.com", LocalDateTime.now());
        customerJdbcRepository.deleteAll();
    }

    @Test
    @Order(1) //test 실행 순서 지정
    public void testHikariConnetionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 등록할 수 있다.")
    public void testInsert() {
//        @BeforeAll 으로 옮김
//        var newCustomer = new Customer(UUID.randomUUID(), "testtest-user", "testtest-user@gmail.com", LocalDateTime.now());
        var result = customerJdbcRepository.insult(newCustomer);

        System.out.println("newCustomer Id => " + newCustomer.getCustomerId());
        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
        //samePropertyValueAs: 두 객체가 같은지 비교해주는 메소드.
    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() throws InterruptedException {
        var customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
        /*
        Thread.sleep(15000);
        show status like '%Threads%';

         */
    }

    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다")
    public void testFindByName() throws InterruptedException{
        var customer = customerJdbcRepository.findByName(newCustomer.getName());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJdbcRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
        Thread.sleep(10000);
    }


    @Test
    @Order(5)
    @DisplayName("이메일로 고객을 조회할 수 있다")
    public void testFindByEmail() throws InterruptedException{
        var customer = customerJdbcRepository.findByEmail(newCustomer.getEmail());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJdbcRepository.findByEmail("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(6)
    @DisplayName("고객을 수정할 수 있다.")
    public void testUpdate() {
        newCustomer.changeName("updated-user");
        var result = customerJdbcRepository.update(newCustomer);

        var all = customerJdbcRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all, samePropertyValuesAs(newCustomer));

        var retrivedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrivedCustomer.get(), samePropertyValuesAs(newCustomer));
    }




}