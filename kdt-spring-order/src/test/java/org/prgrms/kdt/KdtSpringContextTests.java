package org.prgrms.kdt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.prgrms.kdt.configuration.AppConfiguration;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.order.OrderStatus;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.MemoryVoucherRepository;
import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/*      <<Integration Test(통합테스트)>>
    - Mock 이나 stub 을 이용한 것이 아닌 실제 Bean 생성, 의존관계 형성 후 상태와 동작을 검증(test 환경으로)
    - 실제 생성된 객체 간의 상호 협력(의존)관계를 통합 테스트

* SpringExtension 을 이용하면 spring test context framework 을 사용할 수 있도록 함.
* @ExtendWhith: SpringExtension 이 JUnit5과 연동하여 동작하기 위함
* @ContextConfiguration: 어떤 식으로 application configuration 이 만들어져야 하는지만 알려줌
 * */
//@ExtendWith(SpringExtension.class) //Junit 과 연동, test context 를 만듦
//@ContextConfiguration //(classes = {AppConfiguration.class}) //configuration 파일을 읽어 context 를 만들어준다.

//@SpringJUnitConfig: @ExtendWith 와 @ContextConfiguration 을 합친 어노테이션.
@SpringJUnitConfig
@ActiveProfiles("test") // test 프로파일을 등록
public class KdtSpringContextTests {

    //contextConfiguration 에 별도로 빈 설정을 정의한 클래스나 xml 을 전달하지 않으면
    // 기본적으로 @Configuration static 을 찾음
    @Configuration
    @ComponentScan(basePackages = {"org.prgrms.kdt.voucher", "org.prgrms.kdt.order"})
    static class Config{
        /*
        @Bean
        VoucherRepository voucherRepository(){
            return new VoucherRepository() {
                @Override
                public Optional<Voucher> findById(UUID voucherId) {
                    return Optional.empty();
                }

                @Override
                public Voucher insert(Voucher voucher) {
                    return null;
                }
            };

        }
         */
    }
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OrderService orderService;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("applicationContext 가 생성되어야 한다.")
    public void testApplicationContext(){
        assertThat(applicationContext, notNullValue());

    }

    @Test
    @DisplayName("VoucherRepository 가 Bean 으로 등록되어 있어야 한다.")
    public void testVoucherRepositoryCreation(){
        var bean = applicationContext.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
        /* @ContextConfiguration 에 정의하지 않으면 에러 발생*/
    }
    @Test
    @DisplayName("OrderService 를 사용해서 주문을 생성할 수 있다.")
    public void testOrderService(){
        //Given
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        //When
        var order = orderService.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());

        //Then :: mock 객체가 아닌 실제로 bean 으로 등록된 객체 간의 상호 협력관계를 통합테스트한 것임.
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCECPTED));
    }
}
