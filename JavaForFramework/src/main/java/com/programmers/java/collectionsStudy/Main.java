package com.programmers.java.collectionsStudy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        //List의 구상체가 LinkedList, ArrayList이기 때문에 둘 다 가능
        List<Integer> list1 = new LinkedList<>();
        List<Integer> list2 = new ArrayList<>();

        list1.add(1);
        list1.add(2);
        list1.add(3);

        //for(int i = 0; i<list1.size(); i++) System.out.println(list1.get(i));
        for (Integer integer : list1) System.out.println(integer);

        //MyCollection
        //list.forEach()기능을 구현
        new MyCollection<>(Arrays.asList(1, 2, 3, 4, 5))
                .foreach(System.out::println);

        //MyCollection<T>, 제너릭으로 받기 때문에 Array에 들어오는 type에 따라 T가 결정되고 Consumer도 type T에 맞춰진다.
        new MyCollection<>(Arrays.asList("A", "C", "B", "C", "E")).foreach(System.out::println);

        /*
        MyCollection<String> c1 = new MyCollection<>(Arrays.asList("A", "CA", "DSB", "QEFC", "ABABE"));
        c1.foreach(System.out::println);
        MyCollection<Integer> c2 = c1.map(String::length);
        c2.foreach(System.out::println);
        위의 코드를 아래와 같이 변경
        .filter추가: 짝수인 경우에만 출력하겠다
         */
        //method chaining: 함수가 연결되는 방식
        new MyCollection<>(Arrays.asList("A", "CA", "DSB", "QEFC", "ABABE"))
                .map(String::length)
                .filter(i -> i % 2 == 0)
                .foreach(System.out::println);

        //문자열의 길이가 홀수인 것의 개수
        int size = new MyCollection<>(Arrays.asList("A", "CA", "DSB", "QEFC", "ABABE"))
                .map(String::length)
                .filter(i -> i % 2 == 1)
                .size();
        System.out.println(size);
        //함수의 기능을 호출부에서 결정함으로써 MyCollection을 범용적으로 사용하며, 변화 없이 계속 재활용이 가능하게 되었다.


        //User를 만들고 19세 이상만 출력, 주소값을 유저 이름으로 바꿈(map)
        new MyCollection<User>(
                Arrays.asList(
                        new User(15, "AAA"),
                        new User(16, "BBB"),
                        new User(17, "CCC"),
                        new User(18, "DDD"),
                        new User(19, "EEE"),
                        new User(20, "FFF"),
                        new User(21, "GGG"),
                        new User(22, "HHH"),
                        new User(23, "III")
                )
        )
                .filter(u -> u.getAge() >= 19)
                .map(User::getName)
                .foreach(System.out::println);

        //.filter(u -> u.getAge() >= 19)를 19세 이상인지 검사하는 메소드로 변경(User클래스)
        new MyCollection<User>(
                Arrays.asList(
                        new User(15, "AAA"),
                        new User(16, "BBB"),
                        new User(17, "CCC"),
                        new User(18, "DDD"),
                        new User(19, "EEE"),
                        new User(20, "FFF"),
                        new User(21, "GGG"),
                        new User(22, "HHH"),
                        new User(23, "III")
                )
        )
                .filter(User::isOver19)
                .map(User::getName)
                .foreach(System.out::println);

        //getName으로 출력하는 것이 아니라 User객체에서 toString 오버라이드
        //User 클래스의 내용은 User클래스가 책임을 갖고 수행
        //User 클래스 필드의 정보를 노출하지 않고 기능을 수행할 수 있게 됨
        new MyCollection<User>(
                Arrays.asList(
                        new User(15, "AAA"),
                        new User(16, "BBB"),
                        new User(17, "CCC"),
                        new User(18, "DDD"),
                        new User(19, "EEE"),
                        new User(20, "FFF"),
                        new User(21, "GGG"),
                        new User(22, "HHH"),
                        new User(23, "III")
                )
        )
                .filter(User::isOver19)
                .foreach(System.out::println);
    }
}
