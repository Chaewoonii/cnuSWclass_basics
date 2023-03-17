package org.prgrms.kdt.customer;

import com.wix.mysql.EmbeddedMysql;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.prgrms.kdt.customer.domain.Customer;
import org.prgrms.kdt.customer.repository.CustomerJdbcTemplateRepository;
import org.prgrms.kdt.customer.repository.CustomerNamedJdbcTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //Order annotation 의 순서로 실행하겠다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // test instance lifecycle은 클래스 당 하나임을 지정.
                                                // -> 지정하지 않으면 clean을 할 때 static이어야 하는데(전부 지워야 하므로) 클래스당 하나로 지정하면 static 일 필요가 없음.
class CustomerNamedJDBCRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcTemplateRepository.class);

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )
    static class Config{
        @Bean
        public DataSource dataSource(){

            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test_order_mgmt")
                    .username("test")
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);
            return dataSource;
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
            return new NamedParameterJdbcTemplate(dataSource);
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Autowired
    CustomerNamedJdbcTemplateRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    EmbeddedMysql embeddedMysql;


    @BeforeAll
    void setUp(){
        newCustomer = new Customer(UUID.randomUUID(), "testtest-user", "testtest-user@gmail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

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
    @Order(1)
//    @Disabled
    public void testHikariConnetionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }


    @Test
    @Order(2)
    @DisplayName("고객을 등록할 수 있다.")
    public void testInsert() {
        try{
            customerJdbcRepository.insert(newCustomer);
        }catch(BadSqlGrammarException e){
            logger.error("Got BadSqlGrammarException error code -> {}",e.getSQLException().getErrorCode(), e);
        }

        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() throws InterruptedException {
        var customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));

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