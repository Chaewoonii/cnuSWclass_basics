package org.prgrms.kdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.prgrms.kdt.customer")
public class SimpleKdtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleKdtApplication.class, args); //Spring boot은 embeded Tomcat을 통해 별도의 설정 없이도 서버를 띄운다.
    }
}
