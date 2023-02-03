package org.prgrms.kdt.order;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class OrderProperties implements InitializingBean {

    /*
    - @Value: 생성자를 통해 필드를 초기화하지 않아도 값이 주입됨
    - @Value 안의 값은 String 으로 작성해야한다.(문자열을 읽은 타입변환하여 주입하기 때문)
        >> @Value(1):::error, @Value("1"):::Ok
    - @Value를 지정하지 않았을 경우, 기본적으로 int는 0, Integer는 null이 됨.
    - int 필드에 value로 "abc" 등 문자열을 지정하는 등, 타입변환이 불가능할 경우 예외가 발생한다.
    - Value("${}"): {}안에 .properties에서 작성한 property key를 지정하여 넣을 수 있다.
    - ${kdt.version3}과 같이 .properties에 작성하지 않은 property를 가져올 경우 그 전체가 문자열로 전달된다
        >> ${kdt.version} -> v1.0.0 출력
        >> ${kdt.version3} -> "${version3}" 출력
    - ${kdt.version3:v0.0.0} : 뒤에 해당 property가 없을 경우 주입하는 default 값을 적는다.
        >> ${kdt.version3:v0.0.0} -> v0.0.0 출력
        >> ${kdt.version:v0.0.0} -> v1.0.0 출력

    - JAVA_HOME: 시스템 환경변수를 사용하고 싶을 경우, ${환경변수}와 같이 작성하면 된다
        >> ${JAVA_HOME} -> C:\Program Files\Java\jdk-17.0.5 출력

    ※우선순위: 시스템 환경변수 > property
        >> .properties에서 작성한 property의 이름이 시스템 환경변수의 이름과 같을 경우,
        시스템 환경변수의 값이 반환된다. 즉, 시스템 환경변수가 property 보다 더 높은 우선순위에 있다.
    */
    //@Value("v1.1.1") //직접 지정
    @Value("${kdt.version3:v0.0.0}") //property를 가져옴
    private String version;

    @Value("${kdt.minimum-order-amount}")
    private int minimumOrderAmount;

    @Value("${kdt.support-vendors}")
    private List<String> supportVendors;

    @Value("${JAVA_HOME}")
    private String javaHome;


    //InitializingBean 통해 객체 생성 시 필드에 접근해서 정보를 얻음
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(MessageFormat.format("version -> {0}", version));
        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", minimumOrderAmount));
        System.out.println(MessageFormat.format("supportVendors -> {0}", supportVendors));
        System.out.println(MessageFormat.format("javaHome -> {0}", javaHome));
    }
}
