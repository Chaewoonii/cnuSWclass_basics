package com.programmers.java.optional;

import java.util.Optional;

public class Main2 {
    public static void main(String[] args) {
        /*
        EMPTY값을 사용하는 것은 그 context를 아는 사람들만 사용할 수 있음. 모르는 사람은 사용할 수 없음
         >> optional을 사용.
        Optional: null이 될 수도, 아닐수도 있는 값들을 운반해주는 객체
        값이 없을 땐 Optional.empty()를 사용하고, 값을 넣을 땐 Optional.of()를 사용
         */

        //이 객체는 null(.empty())
        Optional<User> optionalUser = Optional.empty();

        //객체에 정보를 담을 경우
        optionalUser = Optional.of(new User(1, "홍길"));

        optionalUser.isEmpty(); //값이 없으면(null이면) true
        optionalUser.isPresent(); //값이 있으면 true

        //코딩 방식
        if (optionalUser.isPresent()){
            // do 1
        }else {
            // do 2
        }

        //위와 같은 결과
        if (optionalUser.isEmpty()){
            // do 2
        }else{
            // do 1
        }

        optionalUser.ifPresentOrElse(user -> {
            // do 1
        }, () ->{
            //do 2
        });

        //위의 두 기능을 하는 함수
        optionalUser.ifPresent(user -> {
            //do 1
        });


    }
}
