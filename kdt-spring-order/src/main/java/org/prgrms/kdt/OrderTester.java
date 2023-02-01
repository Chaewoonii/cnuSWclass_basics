package org.prgrms.kdt;

import org.prgrms.kdt.AppConfigurateion;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        /*//annotaionContext로부터 Bean을 생성해 orderService를 만듦.
        var applicationContext = new AnnotationConfigApplicationContext(AppConfigurateion.class);

        var customerId = UUID.randomUUID();
        var orderService = applicationContext.getBean(OrderService.class);
//        var orderContext = new AppConfigurateion();
//        var orderService = orderContext.orderService();
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }});
        Assert.isTrue(order.totalAmount() == 100L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));
        */

        var applicationContext = new AnnotationConfigApplicationContext(AppConfigurateion.class);
        //고객 생성
        var customerId = UUID.randomUUID();

        /*
        - Qualifier가 설정되었기 때문에 repository 인스턴스를 생성할때도 지정해줘야 한다. getBean도 어떤 bean을 갖고와야할지 모르기 때문
        - 각 bean의 이름을 다르게 하거나, BeanFactoryAnnotationUtils을 이용해서 가져온다.
        - 실제로 BeanFactoryAnnotationUtils를 사용해 qualifier를 지정하고 가져오는 경우는 거의 없다.
        - 대체로 Qualifier를 쓰는 경우는 여러개의 서버에 동일한 repository를 할당해야되는데 동일하기 때문에 충돌이 일어나는 경우이다.
        - 보통은 primary키워드를 주며, 사용하는 쪽에서 고민하지 않게 하는 쪽이 좋다.
         >> 아래의 코드를 변경
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);

                                                                      applicationContext가 아닌 BeanFactory를 요구                            qualifier 지정  */
        var voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");

        /*
          싱글톤 패턴
           : 생성자가 여러 차례 호출되더라도 최초의 생성자가 생성한 객체를 리턴. 실제로 생성되는 객체는 단 하나.
           공통된 객체를 여러개 생성해서 사용하는 DBCP 상황에서 많이 사용.
           (DBCP; DatatBase Connection Pool)

         - Xml이나 @Bean을 통해 Bean defintion을 설정한다
         - 이 Bean defintion으로부터 Bean이 한개가 만들어질 수도, 여러 개가 만들어질 수도 있다.
         - 단 하나만 만들어지는 것을 싱글톤이라고 하고, 스프링 빈은 기본적으로 싱글톤 스콥이다.
         - Spring Bean은 6개의 Bean scope가 있다.

        var voucherRepository2 = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");
        System.out.println(MessageFormat.format("voucherRepository{0}", voucherRepository));    //주소값
        System.out.println(MessageFormat.format("voucherRepository2{0}", voucherRepository2));  //voucherRepository와 같은 주소값
        System.out.println(MessageFormat.format("voucherRepository == voucherRepository2 => {0}", voucherRepository == voucherRepository2));    //true

        - Bean을(객체를) 여러 개 만들고 싶을 경우 해당 클래스(인터페이스)에 @Scope(value="prototype" 또는 ConfigurableBeanFactory.SCOPE_SINGLETON) 을 지정해준다.
        - @Scope을 prototype로 지정한 이후에는 voucherRepository와 voucherRepository2의 주소값이 다르게 나오고, == 연산의 결과도 false이다.
        - voucherRepository를 두개 만들 경우, voucherService가 voucherRepository를 찾을 수 없는 문제가 발생한다.(OrderService.createOrder -> VoucherService.getVoucher)
        - FixedAmountVoucher에서 voucherID를 만들어줌 -> ??
        */

        //바우처 생성
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));
        //오더서비스 객체 생성
        var orderService = applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());
        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));


    }
}
