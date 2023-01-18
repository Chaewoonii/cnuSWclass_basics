package com.programmers.java.baseball.test;

import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        /*
        searh.maven.org에서 JavaFaker검색, 1.0.2 버전 > gradle dependency 복사
        >> build.gradle의 dependency에 추가
        >> gradle 리프레시 버튼을 눌러주면 빌드툴이 알아서 다운로드해줌.
        faker: 가짜 정보를 만들어줌
        * */
        Faker faker = new Faker();
        String title  = faker.name().fullName(); //사람 이름이 랜덤하게 바뀌면서 계속 결과값을 가져옴
        System.out.println(title);

        String character = faker.starTrek().character(); //스타트랙의 등장인물
        System.out.println(character);

        String loc = faker.starTrek().location(); //스타트랙의 지명
        System.out.println(loc);

        long r = faker.number().randomNumber();
        System.out.println(r);

        //1부터 9까지 0을 제외한 숫자 10개 출력
        Stream.generate(() -> faker.number().randomDigitNotZero())
                .limit(10)
                .forEach(System.out::println);

        //숫자 3개 뽑고(중복x) array에 담아 출력
        Integer[] nums = Stream.generate(() -> faker.number().randomDigitNotZero())
                .distinct()
                .limit(3)
                .toArray(Integer[]::new);
        System.out.println(Arrays.toString(nums));

    }
}