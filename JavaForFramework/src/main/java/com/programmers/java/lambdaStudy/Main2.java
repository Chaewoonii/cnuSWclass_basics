package com.programmers.java.lambdaStudy;

import java.lang.module.Configuration;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main2 {
    public static void main(String[] args) {
        //호스트
        //new Main2().loop(10, System.out::println);

        /*
        * i -> i % 2 == 0: Predicate.test: i값이 들어오면 2로 나눈 나머지가 0인지 테스트
        * System.out::println: Consumer.accept: i값을 받아서 출력
        * */
        new Main2().filteredNumbers(30,
                i -> i%2 == 0,
                System.out::println
                );
    }

    //루프를 돌고 i로 무언가를 수행하는것은 호스트가 결정
    //나는 루프만 수행할테니 동작은 호스트가 결정하세요
    void loop(int n, MyConsumer<Integer> consumer){
        for (int i = 0; i < n; i++){
            //i를 주고 뭔가 해라!
            // sum += i 를 넣을 경우, 누적 기능밖에 수행하지 못함
            // 입력은 있고, 출력은 따로 없어도 된다
            consumer.consume(i);
        }
    }

    //자바 내장 함수 이용(java.util.function)
    //Predicate: 어떤 타입이든지 무언가가 들어오면 test후 boolean으로 반환
    //Consumer(functionalInterface): 특정 type의 변수를 accept하겠다. 호출부에서 accept함수는 람다 표현식으로 구현(MyConsume)
    //Function<T, R>은 MyMapper<IN, OUT>/ MySupplier은 Supplier, Runnable은 Runnable
    void filteredNumbers(int max, Predicate<Integer> p, Consumer<Integer> c) {

        for (int i = 0; max > i; i++) {
            if (p.test(i)) c.accept(i);
        }
    }
}
