package com.programmers.java.baseball;

public class MainLombok {
    public static void main(String[] args) {

        //롬복의 AllArgsConstructor를 활용해서 생성자를 만듦
        User user = new User(1, "홍길동");

        //롬복에서 ToString을 처리, 주소값이 아닌 객체의 정보가 출력
        System.out.println(user);

        //롬복에서 Equals를 처리, 두 객체가 같은지 판별
        User user2 = new User(1, "홍길동");
        System.out.println(user.equals(user2)); //true

        //롬복에서 만들어준 getter로 이름과 나이를 가져옴
        System.out.println(user.getName());
        System.out.println(user.getAge());

        //롬복에서 setter 생성
        user.setAge(20);
        System.out.println(user);

    }
}
