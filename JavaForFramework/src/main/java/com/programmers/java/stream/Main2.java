package com.programmers.java.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main2 {
    public static void main(String[] args) {
        /*스트림 만들기*/
        //Stream의 generic으로 만들어짐. Integer에 대한 stream을 generic으로 만들 수는 있지만 int를 generic로 만들 수는 없음)
        Stream<Integer> s = Arrays.asList(1,2,3).stream(); //List에 내장된 기능. stream으로 변환

        //array는 객체가 아니라 stream이라는 메소드가 있지는 않음.
        //그러나, Arrays.Util 클래스에 stream 메소드 존재.
        Arrays.stream((new int[]{1, 2, 3}));

        //toList: 리스트로. collect 기능을 사용
        Arrays.stream(new int[]{1,2,3}).boxed().collect(Collectors.toList());

        //toArray에 타입을 지정해주지 않으면 Object타입이 반환됨
        Arrays.stream(new int[]{1,2,3}).boxed().toArray(Integer[]::new);

        //Stream 만들기
        //1이 계~속 출력됨. 만들어둔 값을 출력하는 것이 아니라 generate를 통해 1을 계속 만듦
        //Stream.generate(() -> 1).forEach(System.out::println);

        //limit를 통해 generate에 제한을 둠. 10개만 출력
        Random r = new Random();
        Stream.generate(r::nextInt)
                .limit(10)
                .forEach(System.out::println);

        /*iterate는 인자 두개를 받는다.
          seed: 초기(seed)값
          함수: seed값에 적용할 함수*/
        Stream.iterate(0, (i) -> i + 1)
                .limit(10)
                .forEach(System.out::println);


        // 주사위를 100번 던져서 6이 나올 확률을 정하시오
        // 0부터 5까지로 bound, +1을 해서 1부터 5까지 출력
        // filter: 어떤 값이 6인 것만 true
       var count = Stream.generate(() -> r.nextInt(6) + 1)
                .limit(100)
                .filter(n -> n == 6)
                .count();
        System.out.println(count);


        //1~9 사이 값 중에서 겹치지 않게 3개를 출력
        //distinct: 중복 검사
        //map은 object타입에서 object타입으로 반환, mapToInt는 IntStream으로 반환
        //따라서, toArray를 하면 Int의 Array타입으로 바로 결과가 나옴.(Object 타입에서는 toArray에 타입을 제공해야했음)
        var arr = Stream.generate(() -> r.nextInt(10) + 1)
                .distinct()
                .limit(3)
                .mapToInt(i -> i)
                .toArray();

        //그냥 arr를 출력하면 주소값이 출력됨
        System.out.println(Arrays.toString(arr));

        //0~200 사이 값 중에서 랜덤값 5개를 뽑아 큰 순서대로 표시하시오.
        int[] arr2 = Stream.generate(() -> r.nextInt(200))
                .limit(5)
                .sorted(Comparator.reverseOrder())
                .mapToInt(i ->i)
                .toArray();
        System.out.println(Arrays.toString(arr2));

    }
}
