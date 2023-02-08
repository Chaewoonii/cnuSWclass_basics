package org.prgrms.kdt.voucher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//메모리에서 관리되는 repository
@Repository
//@Primary // voucherRepository를 상속받는 동일한 Repository가 두개: 둘 중에 어떤 것에 의존관계를 설정할지 지정해줘야함. @Primary
@Profile("local") //같은 repository이지만 쓰임이 다를 경우 이름을 준다. 생성자에 어떤 것의 의존을 받는지 (동일한 어노테이션을 통해)설정
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) //SCOPE_PROTOTYPE(value = "prototype") 가능. Bean을 여러 개 생성할 수 있도록 설정
                                                        //SCOPE_SINGLETON: Bean을 하나만 생성
public class MemoryVoucherRepository implements VoucherRepository, InitializingBean, DisposableBean {

    //메모리에서 관리: 해시맵 사용. thread-safety를 위해 Concurrency를 사용
    private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        return Optional.ofNullable(storage.get(voucherId));
    }

    @Override
    public Voucher insert(Voucher voucher) {
        storage.put(voucher.getVoucherId(), voucher);
        return voucher;
    }

    //생성 콜백 1
    @PostConstruct
    public void postConstruct(){
        System.out.println("postConstruct called!");
    }


    //InitializingBean, 생성콜백 2
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }


    //소멸 콜백 1
    @PreDestroy
    public void preDestroy(){
        System.out.println("preDestroy called!");
    }
    //소멸 콜백 2
    @Override
    public void destroy() throws Exception {
        System.out.println("destroy called!");
    }
}
