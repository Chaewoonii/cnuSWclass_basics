package com.programmers.java.lambdaStudy;

import java.lang.module.Configuration;

public class Main2 {
    public static void main(String[] args) {
        //호스트
        new Main2().loop(10, System.out::println);
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
}
