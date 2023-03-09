package org.prgrms.kdt.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class CalculatorImpl implements Calculator{

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}
interface Calculator{
    int add(int a, int b);
}

class LoggingInvocationHandler implements InvocationHandler{

    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    private final Object target;

    public LoggingInvocationHandler(Object target){
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*
        * CalculatorImpl 이 target이되고 target의 method가 invoke를 실행
        * CalculatorImpl의 add 메소드가 invoke를 실행한다.
        * */

        log.info("{} executed in {}", method.getName(), target.getClass().getCanonicalName());
        return method.invoke(target, args);
    }
}


public class JdkProxyTest {
    private static final Logger log = LoggerFactory.getLogger(JdkProxyTest.class);
    public static void main(String[] args) {
        var calculator = new CalculatorImpl();

        /*
        Dynamic Proxy: proxy instance 생성. invocation handler 전달해야함.
          - invocation handler가 (interface) Calculator를 다루는 proxy instance를 생성
          - proxy instance는 (interface) Calculator의 구현체 CalculatorImpl을 target으로 함.

        JDK Dynamic Proxy
          : Dynamic Dispatch
          : 동적 디스패치: 컴파일 시에는 Calculation interface type
          실행 시 실제 객체에 담긴 클래스에 따라 proxy target 이 결정됨.
          Calculation을 상속하는 구현 클래스가 CalculatorImpl1, CalculatorImpl2 두개일 경우,
          CalculatorImpl1.add()를 실행할지 CalculatorImpl2.add()를 실행할지 실행 시점에서 결정됨.

          마찬가지로 proxy target object 도 CalculatorImpl1인지 CalculatorImpl2인지 실행 시점에 결정됨
          Calculator calculator = new CalculatorImpl1();
          * target object로 인터페이스 calculator 전달
            >> 컴파일: Calculator type
            >> 실행 시점, proxy 인스턴스 생성
                -> calculator에 담긴 클래스는 CalculatorImpl1
                --> target object가 CalculatorImpl1인 proxy instance가 생성됨
        */
         Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(
                LoggingInvocationHandler.class.getClassLoader(),
                new Class[]{Calculator.class},
                new LoggingInvocationHandler(calculator));

         //Proxy test
         var res = proxyInstance.add(1, 2);
         log.info("Add -> {}", res);

    }
}
