package org.prgrms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.MemoryVoucherRepository;
import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    //스텁
    class OrderRepositoryStub implements OrderRepository{

        @Override
        public Order insert(Order order) {
            return null;
        }
    }

    @Test
    @DisplayName("오더가 생성되어야 한다. (stub)")
    void createOrder() {
        //Given: 이런 상황에서
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
        var sut = new OrderService(new VoucherService(voucherRepository), new MemoryOrderRepository());

        //When: 어떤 메소드가 호출되었을 때
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //Then: 검증. 이런 상태가 되어야 한다.
        assertThat(order.totalAmount(), is(100L)); //Long으로 정확히 기술

        //optional: voucher 가 있을수도, 없을수도 있음. voucher는 당연히 있다고 가정, .isEmpty는 false여야 한다.
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId())); //주입되었으므로 id가 같아야 함
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCECPTED));
    }

    @Test
    @DisplayName("오더가 생성되어야 한다. (mock)")
    void createOrderByMock() {
        //Given
        // Mockito의 mock()
        var voucherServiceMock = mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);

        //stub은 실제 객체가 만들어지지만 mock은 when만 동작
        //when: VoucherService.getVoucher가 호출되면
        //thenReturn: FixedAmountVoucher가 반환되어야한다.
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);
        var sut = new OrderService(voucherServiceMock, orderRepositoryMock);

        //When: 어떤 메소드가 호출되었을 때
        var order = sut.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());


        //Then: 검증: 행위 관점. 메소드가 의도한 대로 정상적으로 호출되는지 verify
        assertThat(order.totalAmount(), is(100L)); // 상태 검증
        assertThat(order.getVoucher().isEmpty(), is(false)); // 상태 검증

        //특정한 순서로 호출되는지 확인.
        var inOrder = inOrder(voucherServiceMock, orderRepositoryMock);
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        inOrder.verify(orderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);

        //순서 확인 x
//        verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId()); //행위 검증: when에서 getVoucherId가 호출되었는지 확인.
//        verify(orderRepositoryMock).insert(order); //행위 검증: orderRepository에 들어갔는지. insert가 호출되었는지.
//        verify(voucherServiceMock).useVoucher(fixedAmountVoucher); //행위 검증: voucher가 쓰였음을 나타내는 메소드 호출되었는지.

    }



}