package org.prgrms.kdt.order;

import org.prgrms.kdt.configuration.VersionProvider;
import org.prgrms.kdt.voucher.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/*  @Service는 AppConfiguration의 다음 코드를 대체한다. 즉, Bean을 자동 생성/등록한다.
    @Bean
    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository){
        return new OrderService(voucherService, orderRepository);
    }
* */
@Service
public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;
    //private final VersionProvider versionProvider;

    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
        //this.versionProvider = versionProvider;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems){
        var order =  new Order(UUID.randomUUID(), customerId, orderItems);
        return orderRepository.insert(order);
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId){
        //versionProvider.getVersion(); //VersionProvider를 통해 버전정보를 가져옴. versionProvider는 setter가 없기 때문에 runtime에서 바꾸지 못함.
        var voucher = voucherService.getVoucher(voucherId);
        var order =  new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        orderRepository.insert(order);
        voucherService.useVoucher(voucher);
        return order;
    }

}
