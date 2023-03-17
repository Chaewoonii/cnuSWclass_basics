package org.prgrms.kdt.servlet;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.prgrms.kdt.customer.controller.CustomerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KdtWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);


    //servlet Application context에 사용될 config, 웹(Spring) MVC 관련 설정
    @EnableWebMvc //Spring MVC가 필요한 bean들이 자동으로 등록됨
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class),
            useDefaultFilters = false
    )//includeFilters: CustomerController 만 assign. useDefaultFilters: component scan 시 이미 include 했음에도 불구하고 다른 스테레오타입 annotation class들이 등록되는 것 방지
    @EnableTransactionManagement
    static class ServletConfig implements WebMvcConfigurer, ApplicationContextAware {

        //ApplicationContextAware, setApplicationContext에 쓰임
        ApplicationContext applicationContext;

        //WebMvcConfigurer 상속
        //View Resolver 관련 설정: 특정한 view resolver 를 setup 할 수 있음.
        //특별한 설정이 없으면 /WEB-INF/ 폴더 내 해당 view 이름의 jsp 파일을 찾는다.
        // >> ViewResolverRegistry.jsp()에 설정됨.
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.jsp().viewNames("jsp/*"); //jstl :: jsp 폴더 아래 jsp파일 찾음

            //Thymeleaf
            //ApplicationContext-> SpringTemplateResolver -> SpringTemplateEngine -> ThymeleafViewResolver -> registry.viewResolver(thymeleafViewResolver)
            var springResourceTemplateResolver = new SpringResourceTemplateResolver();
            //spring template resolver는 application context를 꼭 전달해야 함:::ApplicationContextAware 상송 -> setApplicationContext로 applicationContext 설정
            springResourceTemplateResolver.setApplicationContext(applicationContext);
            springResourceTemplateResolver.setPrefix("/WEB-INF/"); //폴더
            springResourceTemplateResolver.setSuffix(".html"); //확장자 :: jsp는  /WEB-INF/jsp/.jsp
            var springTemplateEngine = new SpringTemplateEngine(); // Spring template Engine
            springTemplateEngine.setTemplateResolver(springResourceTemplateResolver); //template engine 에도 template을 찾을 수 있게 template resolver를 넣어줘야 함.


            var thymeleafViewResolver = new ThymeleafViewResolver(); // thymeleaf view resolver
            thymeleafViewResolver.setTemplateEngine(springTemplateEngine); //template engine을 넣어야 함 ->> spring engine 을 쓸 수 있게 함
            thymeleafViewResolver.setOrder(1); //view resolver는 chain 형태임 ->> 순서를 설정해줘야 한다.
            thymeleafViewResolver.setViewNames(new String[]{"views/*"}); //jstl의 view resolver와 충돌 발생
                                                    // ->> 이름 설정: views 폴더 아래에 있는 view에 thymeleaf view resolver를 적용하겠다.
            registry.viewResolver(thymeleafViewResolver); //thymeleaf view resolver 를 view resolver에 추가
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

        @Override //ApplicationContextAware 의 method override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

/*        @Override //모든 HTTP Message Converter를 override. 모든 response가 xml로 변환됨. json으로 반환받고 싶으면 추가해줘야함.
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            //Xml :: HTTP Message Converter가 data를 XML로 변환. (default는 json)
            var messageConverter = new MarshallingHttpMessageConverter();
            var xStreamMarshaller = new XStreamMarshaller();
            messageConverter.setMarshaller(xStreamMarshaller);
            messageConverter.setUnmarshaller(xStreamMarshaller);

            converters.add(messageConverter); //HTTP Message Converter 추가
        }*/

        @Override //configureMessageConverters 가 아닌 extendMessageConverters 를 이용하면 response type에 xml이 추가됨. 즉, json 반환도 가능.
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            //xml
            var messageConverter = new MarshallingHttpMessageConverter();
            var xStreamMarshaller = new XStreamMarshaller();
            messageConverter.setMarshaller(xStreamMarshaller);
            messageConverter.setUnmarshaller(xStreamMarshaller);

            //index 0: xml 형식의 message converter 를 제일 앞에 둠
            converters.add(0, messageConverter);

            //json
            // java object를 json으로 바꿀 때 Jackson2ObjectMapper 를 많이 씀
            var javaTimeModule = new JavaTimeModule(); // createdAt이 list 형식으로 전달되기 때문에 time 모듈을 설정
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
            var modules = Jackson2ObjectMapperBuilder.json().modules(javaTimeModule);
            converters.add(1, new MappingJackson2HttpMessageConverter(modules.build()));

        }

        @Override //CORS 허용 설정:: WebMvcConfigurer
        public void addCorsMappings(CorsRegistry registry) {
            // api 이하 모든 url에 대하여 HTTP Method 중 GET, POST 만 허용, 모든 origin 허용
            registry.addMapping("/api/**").allowedMethods("GET","POST").allowedOrigins("http://localhost:3000");
        }
    }



    //Root Application Context에 사용될 config, DataSource 등
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class)
    )//excludeFilters: CustomerController 빼고 나머지가 Bean으로 등록
    @EnableTransactionManagement
    static class RootConfig{
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


        //RootApplicationContext:::ContextLoaderListener
        var rootApplicationContext = new AnnotationConfigWebApplicationContext();
        rootApplicationContext.register(RootConfig.class);
        var loaderListener = new ContextLoaderListener(rootApplicationContext);
        servletContext.addListener(loaderListener);


        //servlet 생성 및 등록: application context 생성, AppConfig 등록, dispatcher servlet 생성, servlet context에 등록.
        var applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ServletConfig.class); //applicationContext에 configuration class 전달

        var dispatcherServlet = new DispatcherServlet(applicationContext); //application context를 dispatcher servlet에 전달
        var servletRegistration = servletContext.addServlet("test", dispatcherServlet); // Servlet 추가
        servletRegistration.addMapping("/"); //모든 요청을 DispatcherServlet이 받음
        servletRegistration.setLoadOnStartup(-1); //요청이 들어오면 서블릿 인스턴스를 생성
    }
}
