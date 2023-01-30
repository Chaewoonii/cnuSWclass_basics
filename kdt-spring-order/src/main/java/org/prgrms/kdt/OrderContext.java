package org.prgrms.kdt;

import java.util.Optional;
import java.util.UUID;

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
public class OrderContext {

    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }

    public OrderRepository orderRepository(){
        return new OrderRepository() {
            @Override
            public void insert(Order order) {
            }
        };
    }
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    public OrderService orderService(){
        return new OrderService(voucherService(), orderRepository());
    }
}
