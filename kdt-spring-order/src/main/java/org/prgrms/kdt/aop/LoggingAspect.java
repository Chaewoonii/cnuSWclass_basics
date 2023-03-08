package org.prgrms.kdt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /*Around(포인트컷 표현식)
    포인트컷 지정자: 어드바이저를 어떻게 적용시킬지 aop에게 알려줌
    execution(): 메소드 실행 시점에 어떻게 하겠다. 포인트컷 패턴을 어떻게 적용시키겠다. 가장 기본적
    execution(접근지정자 리턴타입 클래스)
    execution(public * org.prgrms.kdt..*Repository)
          - public 메소드
          - *: 모든 리턴타입
          - org.prgrms.kdt..*Repository: org.prgrms.kdt 패키지 안의 모든 패키지, Repository로 끝나는 클래스에
          어드바이저를 적용

    org.prgrms.kdt..*.*()
        - org.prgrms.kdt 아래 전체 패키지, 전체 클래스, 전체 메소드에 적용

    org.prgrms.kdt..*Repository.find*()
        - Repository로 끝나는 클래스의 find로 시작하는 메소드에 적용

    org.prgrms.kdt..*.*(..) : 모든 클래스 모든 메소드, 인자로 어떤 것을 받는지 상관 없음
    org.prgrms.kdt..*.*(long, ..): 모든 클래스의 메소드 중 첫 번째 인자를 long으로 받는 것

     */

//    @Around("execution(org.prgrms.kdt..*.*(..))")
//    @Around("org.prgrms.kdt.aop.CommonPointcut.repositoryInsertPointcut()")
    @Around("@annotation(org.prgrms.kdt.aop.TrackTime)") //특정 annotation이 부여된 method에만 쓰겠당
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        var startTime = System.nanoTime(); // method 가 호출되고 반환될 때 까지의 시간
        var result = joinPoint.proceed();
        var endTime = System.nanoTime() - startTime; // method 가 호출되고 반환될 때 까지의 시간
        log.info("After method called with result => {} and time taken by {} nanoseconds", result, endTime);
        return result;
    }



}
