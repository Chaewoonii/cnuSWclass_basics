package org.prgrms.kdt.customer;

import com.wix.mysql.EmbeddedMysql;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.config.Charset.UTF8;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //Order annotation 의 순서로 실행하겠다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // test instance lifecycle은 클래스 당 하나임을 지정.
                                                // -> 지정하지 않으면 clean을 할 때 static이어야 하는데(전부 지워야 하므로) 클래스당 하나로 지정하면 static 일 필요가 없음.
class CustomerJDBCRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcTemplateRepository.class);

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )
    static class Config{
//        .type(HikariDataSource):HikariDataSource 로 DataSource 를 만듦.
        @Bean
        public DataSource dataSource(){
            /*
            Embedded Database 의 필요성
              - 테스트 시 DB 연결이 되지 않으면 테스트가 불가능한 상황 발생  >> DB 연결과 상관 없이 테스트를 수행하기 위해 필요.
              - 테스트 시 실제 DB 와 연결되면 데이터 변경 및 삭제의 위험이 따름. >> 실제 DB와 연결하지 않고도 테스트 가능.
            EmbeddedDatabase 는 DataSource 를 extends 하기 때문에 따로 datasource 를 만들지 않고 바로 return.
            EmbeddedDatabase 를 이용할 경우, UUID_TO_BIN 에서 오류가 발생함. 특정 회사의 함수에 종속. sql 을 최대한 표준에 맞춰 작성하거나 Embedded Mysql 을 사용.*/
//            return new EmbeddedDatabaseBuilder()
//                    .generateUniqueName(true)
//                    .setType(EmbeddedDatabaseType.H2)
//                    .setScriptEncoding("UTF-8")
//                    .ignoreFailedDrops(true)
//                    .addScript("schema.sql")
//                    .build();

            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test_order_mgmt")
                    .username("test")
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
//            아무 것도 지정하지 않으면 10개가 minimum 이자 maximum
            dataSource.setMaximumPoolSize(1000); // connection pool 의 최대 connection 개수
            dataSource.setMinimumIdle(100); // connection pool 의 최소 connection 개수. 100개를 기본적으로 만들고 꺼내 쓰겠다.
            return dataSource;
        }

/*        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate){
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }*/

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
            return new NamedParameterJdbcTemplate(dataSource);
        }
    }

//    @Autowired
//    CustomerJDBCRepository customerJdbcRepository;



    @Autowired
    CustomerNamedJdbcTemplateRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    EmbeddedMysql embeddedMysql;

    //테스트 시작 전 딱 한 번 실행
    @BeforeAll
    void setUp(){
        newCustomer = new Customer(UUID.randomUUID(), "testtest-user", "testtest-user@gmail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
//        customerJdbcRepository.deleteAll(); //테스트 시마다 DB를 새로 올리기 때문에 다시 deleteAll 할 필요가 없다.
//
        var mysqlConfig = aMysqldConfig(v5_7_latest)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test","test1234!")
                .withTimeZone("Asia/Seoul")
                .build();

        embeddedMysql = anEmbeddedMysql(mysqlConfig)
                .addSchema("test_order_mgmt", classPathScript("schema.sql"))
                .start();
    }

    @AfterAll
    void cleanUp(){
        embeddedMysql.stop();
    }

    @Test
    @Order(1) //test 실행 순서 지정
//    @Disabled
    public void testHikariConnetionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }


    @Test
    @Order(2)
    @DisplayName("고객을 등록할 수 있다.")
    public void testInsert() {
//        @BeforeAll 으로 옮김
//        var newCustomer = new Customer(UUID.randomUUID(), "testtest-user", "testtest-user@gmail.com", LocalDateTime.now());
//        customerJdbcRepository.deleteAll();

        try{
            customerJdbcRepository.insert(newCustomer);
        }catch(BadSqlGrammarException e){
            logger.error("Got BadSqlGrammarException error code -> {}",e.getSQLException().getErrorCode(), e);
        }


//        System.out.println("newCustomer Id => " + newCustomer.getCustomerId());
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
        assertThat(all.get(0), samePropertyValuesAs(newCustomer));

        var retrivedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrivedCustomer.get(), samePropertyValuesAs(newCustomer));
    }




}