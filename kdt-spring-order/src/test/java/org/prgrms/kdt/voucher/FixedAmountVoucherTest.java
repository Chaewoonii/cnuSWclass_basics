package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*테스트케이스를 만들면서 부족한 부분을 채운다.
* TDD: Test Driven Development: 테스트 기반 개발
* 테스트 케이스를 작성하면서 기능을 완성.
* 테스트 케이스만 봐도 business rule 을 파악할 수 있기 때문에 꼭 테스트케이스를 작성해야 한다.
* */
class FixedAmountVoucherTest {

    private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);

    @BeforeAll // 테스트 실행 전 최초 1회. static으로 작성되어야 함.
    static void setUp(){
        logger.info("@BeforeAll - 단 한 번 실행");
    }

    @BeforeEach // 매 테스트(@Test)마다 initialization.
    void init(){
        logger.info("@BeforeEach - 매 테스트마다 실행");
    }

    //@Test 메서드는 void 이어야 한다.
    @Test
    @DisplayName("기본적인 assertEqual 테스트 😊😊")
    void testAssertEqual(){
        //첫 번째 인자: 기댓값
        //두 번째 인자: 실제 값
        assertEquals(2, 1+1);
    }

    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다.")
    void testDiscount() {
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, sut.discount(1000)); //성공

    }

    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    @Disabled //테스트 스킵
    void testWithMinus() {
        //할인 금액은 -가 될 수 없으므로 에러 발생해야함. assertThrows 로 예외가 발생해야 할 클래스 지정.
        //FixedAmountVoucher에 할인 금액이 -가 될 수 없다는 조건 달아야 함. throw할 클래스도 지정.
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }

    @Test
    @DisplayName("할인된 금액은 마이너스가 될 수 없다")
    void testMinusDiscountedAmount(){
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 1000);
        assertEquals(0, sut.discount(900)); //마이너스가 나옴, 코드 수정 필요.
    }

    @Test
    @DisplayName("유효한 할인 금액으로만 생성할 수 있따.")
    void testVoucherCreation(){
        assertAll("FixedAmountVoucher creation",
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), 10000000)));
    }
}