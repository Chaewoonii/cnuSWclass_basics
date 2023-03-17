package org.prgrms.kdt.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

/*
* IoC; Inversion of Control, 제어의 역전
*   : 제어의 역전이란, 객체가 자신이 사용할 개체를 스스로 선택하지 않고 생성하지도 않는 것을 말한다,
* 프레임워크가 흐름을 주도하면서 개발자가 만든 애플리케이션 코드를 사용하는 것으로,
* (라이브러리를 사용하는 애플리케이션 코드가 흐름을 직접 제어하는 것과는 달리)
* 애플리케이션 코드가 프레임워크에 의해 사용된다.
*
* IoC 컨테이너
*   - IoC 컨테이너에서 개별 객체들의 의존관계 설정이 이루어지며, 객체들의 생성과 파괴를 모두 담당한다.
*   - IoC 컨테이너에 사용할 클래스를 알려주면 객체화시키면서 생성에 대한 필요한 의존관계를 맺어준다.
* */


//IoC 컨테이너
@Configuration

//ComponentScan annotation을 달 경우, 해당 패키지를 스캔하여 필요한 bean을 생성해준다.
@ComponentScan(basePackages = {"org.prgrms.kdt.order","org.prgrms.kdt.voucher", "org.prgrms.kdt.configuration"}) //베이스 페키지 명시. 에러가 발생하는 circulal클래스의 bean은 만들지 않는다.
//@ComponentScan(basePackageClasses = {Order.class, Voucher.class}) //Order클래스가 속한 패키지, Voucher클래스가 속한 패키지를 기준으로 찾게 된다.
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION)}) //제외할 패키지를 어노테이션으로 설정
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = MemoryVoucherRepository.class)}} //제외할 Bean 지정
//@PropertySource("application.yml") //.properties 파일에 작성한 property를 연동
@PropertySource(value = "application.yml", factory = YamlPropertiesFactory.class) //Spring Boot는 yml을 지원, Spring framework는 yml을 지원하지 않음. factory를 만들어 전달해야함.
@EnableConfigurationProperties //ConfigurationProperties: 스프링 부트에서부터 온 것임을 알려줌.
public class AppConfiguration {

    /*
    해당 클래스에 @Repository로 인해 bean이 자동 생성된다.
    @Bean
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }

    @Bean
    public OrderRepository orderRepository(){
        return new OrderRepository() {
            @Override
            public Order insert(Order order) {
                return order;
            }
        };
    }

    */

    /*
    @ComponentScan을 할 경우, 자동으로 만들어짐
    해당 클래스에 @Service로부터 Bean이 자동등록된다.
    @Bean
    public VoucherService voucherService(VoucherRepository voucherRepository){
        return new VoucherService(voucherRepository);
    }

    @Bean
    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository){
        return new OrderService(voucherService, orderRepository);
    }

    */

    /*
    - initMethod = "init": Bean 생성 시 호출할 메소드를 지정. 해당 클래스 내부에 동일한 이름의 메소드가 있어야 함
    - afterPropertiesSet() 호출 후 init()호출
    */
    @Bean(initMethod = "init")
    public BeanOne beanOne(){
        return new BeanOne();
    }

    class BeanOne implements InitializingBean{
        public void init(){
            System.out.println("[BeanOne] init called!");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("[BeanOne] afterPropertiesSet called!");
        }
    }
}
