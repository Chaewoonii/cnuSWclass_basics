package org.prgrms.kdt.servlet;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.prgrms.kdt.customer.CustomerNamedJdbcTemplateRepository;
import org.prgrms.kdt.customer.CustomerRepository;
import org.prgrms.kdt.customer.CustomerService;
import org.prgrms.kdt.customer.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;

import javax.sql.DataSource;

public class KdtWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);

    @EnableWebMvc //Spring MVC가 필요한 bean들이 자동으로 등록됨
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer")
    @EnableTransactionManagement
    static class AppConfig implements WebMvcConfigurer {

        //WebMvcConfigurer 상속
        //View Resolver 관련 설정: 특정한 view resolver 를 setup 할 수 있음.
        //특별한 설정이 없으면 /WEB-INF/ 폴더 내 해당 view 이름의 jsp 파일을 찾는다.
        // >> ViewResolverRegistry.jsp()에 설정됨.
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.jsp();
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/**")
                    .addResourceLocations("/resources/")
                    .setCachePeriod(60)
                    .resourceChain(true)
                    .addResolver(new EncodedResourceResolver());

            //setCachePeriod: 이미지 캐시처리, 60초 동안은 서버에서 다운받지 않게 됨.
            //gzip --keep --best -r src/main/webapp/resources  ->>안됨.
        }

        @Bean
        public DataSource dataSource(){
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/order_mgmt")
                    .username("root")
                    .password("test1234@#")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemaplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate){
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("Starting Server...");

        var applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(AppConfig.class); //applicationContext에 configuration class 전달

        var dispatcherServlet = new DispatcherServlet(applicationContext); //application context를 dispatcher servlet에 전달
        var servletRegistration = servletContext.addServlet("test", dispatcherServlet); // Servlet 추가
        servletRegistration.addMapping("/"); //모든 요청을 DispatcherServlet이 받음
        servletRegistration.setLoadOnStartup(1);
    }
}
