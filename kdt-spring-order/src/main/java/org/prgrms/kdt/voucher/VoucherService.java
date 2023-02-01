package org.prgrms.kdt.voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

/*  @Service는 AppConfiguration의 다음 코드를 대체한다. 즉, Bean을 자동 생성/등록한다.
    @Bean
    public VoucherService voucherService(VoucherRepository voucherRepository){
        return new VoucherService(voucherRepository);
    }
* */
@Service
public class VoucherService {



    /* @Autowired를 통해 의존관계가 자동으로 주입됨
    @Autowired
    private VoucherRepository voucherRepository;

    - 과거에는 생성자에 Autowired를 붙여 사용했음.
    - Spring 4.3부터 Autowired를 붙이지 않아도 자동으로 주입
    - 생성자가 2개 이상일 경우엔 Autowired를 지정해줘야한다: 스프링이 자동으로 주입하는 생성자에 Autowired
    - 스프링에서는 다음의 이유로 생성자에 Autowired를 붙이는 것을 권장
        + 초기화 시 필요한 모든 의존관계가 형성됨: 생성자이므로 필요한 모든 의존관계를 받음(참조할 필드가 없어 NPE가 발생할 가능성을 없앰)(필드가 없을수도 있을땐 optional 타입으로 지정한다)
        + 잘못된 패턴을 찾을 수 있게 도와줌: 많은 파라미터를 가진 생성자(클래스)는 수많은 책임을 가지고 있다는 것을 암시. -> 관심사의 분리가 필요. 잘못된 패턴을 찾음
        + 테스트를 쉽게 해줌: 테스트 시 생성자 주입을 통해 쉽게 객체를 전달할 수 있다.
        + 불변성 확보: final 키워드 -> 한 번 만들어진 의존관계가 변경되지 않도록 도와준다.
    */

    private final VoucherRepository voucherRepository;

    //@Autowired: default
    public VoucherService(@Qualifier("memory") VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Can not find a voucher for {0}", voucherId)));
    }

    public void useVoucher(Voucher voucher) {
    }

    /*
    setter에도 @Autowired가 가능하다. setter에 명시할 경우 빈을 생성할 때 메소드를 추가적으로 호출할수도 있다.
    @Autowired
    public void setVoucherRepository(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }
    */

}
