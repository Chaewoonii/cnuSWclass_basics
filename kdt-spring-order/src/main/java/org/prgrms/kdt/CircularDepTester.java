package org.prgrms.kdt;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


/*
* 순환참조: A->B를 참조하고, B->A를 참조하는 경우
* 순환참조의 경우, Bean이 생성되지 않으니 조심해야한다.
* */
class A{
    private final B b;
    A(B b){
        this.b = b;
    }
}

class B{
    private final A a;

    B(A a) {
        this.a = a;
    }
}

class CircularConfig{

    //A->B를 참조
    @Bean
    public A a(B b){
        return new A(b);
    }

    //B->A를 참조
    @Bean
    public B b(A a){
        return new B(a);
    }
}
public class CircularDepTester {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(CircularConfig.class);
    }
}
