package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
public class HamcrestAssertionTests {

    @Test
    @DisplayName("여러 Hamcrest macher 테스트")
    void hamcrestTest(){
        assertEquals(2, 1+1);
        assertThat(1+1, equalTo(2)); //위의 코드와 동일.
        //assertThat은 실제 값이 먼저 인자로 들어가고, 두 번째 인자로는 matcher 가 들어간다.
        assertThat(1+1, is(2));
        assertThat(1+1, anyOf(is(1), is(2))); //둘 중 하나라도 match 되면 ok.

        // 1+1은 1이 아니다
        assertNotEquals(1, 1+1);
        assertThat(1+1, not(1));
    }

    //리스트에 대한 검증
    @Test
    @DisplayName("컬렉션에 대한 matcher 테스트")
    void hamcrestListMatcherTest(){
        var pirces = List.of(1, 2, 3);
        assertThat(pirces, hasSize(3)); //list 사이즈

        //prices 안의 item이 모두 0보다 큰가?
        assertThat(pirces, everyItem(greaterThan(0)));//성공
//        assertThat(pirces, everyItem(greaterThan(1)));//실패

        //containsInAnyOrder: 순서는 모르겠고 해당 아이템이 존재하는지
        assertThat(pirces, containsInAnyOrder(3, 1, 2));

        //hasItem: 단 하나의 아이템이 존재하는지.
        assertThat(pirces, hasItem(2));

        //2와 같거나 2보다 큰 아이템이 존재하는지.
        assertThat(pirces, hasItem(greaterThanOrEqualTo(2)));

    }
}
