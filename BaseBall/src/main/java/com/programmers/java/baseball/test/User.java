package com.programmers.java.baseball;

import lombok.*;

/*
* 롬복의 사용
* @AllArgsConstructor : 모든 필드정보를 인자로 받는 생성자를 생성
* @ToString: 주소값이 아닌 정보를 출력하는 toString메소드 생성
* @EqualsAndHashCode: 데이터클래스를 만들 때 보통 사용. equals는 인스턴스의 정보가 같은지 검사.
* @Getter: getter 생성
* @Setter: setter 생성
*
* @Data: 위의 모든 것들을 처리, AllArgsConstructor는 제외.
* */

/*@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter*/
@Data
@AllArgsConstructor
public class User {
    private int age;
    private String name;
}
