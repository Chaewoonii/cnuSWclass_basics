package org.prgrms.kdt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    //WebMvcConfigurer 관련 설정: @Configuration 이 붙은 클래스 혹은 @SpringBootApplication 에서 처리할 수 있다.
    //혹은 실행 클래스의 @ComponentScan(basePackages={"org.prgrms.kdt.config"})
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("*");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //xml
        var messageConverter = new MarshallingHttpMessageConverter();
        var xStreamMarshaller = new XStreamMarshaller();
        messageConverter.setMarshaller(xStreamMarshaller);
        messageConverter.setUnmarshaller(xStreamMarshaller);

        //index 0: xml 형식의 message converter 를 제일 앞에 둠
        converters.add(0, messageConverter);

        //javaTimeModule은 Spring에서 알아서 처리해줌.
        /*var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        var modules = Jackson2ObjectMapperBuilder.json().modules(javaTimeModule);
        converters.add(1, new MappingJackson2HttpMessageConverter(modules.build()));*/
    }
}
