package com.programmers.java.optional;

public class Main {
    public static void main(String[] args) {
        /* null을 쓰지 말자는 약속
        * 초기값을 어떻게 주지? null은 사용하지 못하고, 임의의 값을 줄 수도 없음.
        * >> null을 사용하지 않는 방법
        * 1) EMPTY 객체를 사용: User 클래스에 EMPTY 상수 필드를 만들어줌
        * 2) Optional객체를 사용: null이 될수도 아닐수도 있는 객체들을 매핑
        * */
        User user = User.EMPTY;
        User user2 = getUser();
        //user가 if(user2 == null)이 아니라 user2 == User.EMPTY로 정보가 없는 객체인지 아닌지 확인
        //적어도 NPE는 없음
        if (user2 == User.EMPTY){

        }
        System.out.println(user);

    }
    private static User getUser(){
        return User.EMPTY;
    }
}
