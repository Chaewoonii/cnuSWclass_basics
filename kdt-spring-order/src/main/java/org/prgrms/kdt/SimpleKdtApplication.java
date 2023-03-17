package org.prgrms.kdt;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
@PropertySource(value = "application.yml")
@ComponentScan(basePackages = {"org.prgrms.kdt.customer", "org.prgrms.kdt.config"})
public class SimpleKdtApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SimpleKdtApplication.class, args); //Spring boot은 embeded Tomcat을 통해 별도의 설정 없이도 서버를 띄운다.
    }


}
