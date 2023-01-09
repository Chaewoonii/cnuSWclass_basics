package com.programmers.java;

import java.util.ArrayList;
import java.util.List;

//빨간줄 보이면 무조건 Alt+Enter(빨간줄에 커서)
//Runnable: run()메소드 오버라이딩이 필요
//Ctrl+1: 폴더 창으로 커서 옮김
//Ctrl+n: 생성 단축키
//shift두번: 파일 검색이름 검색 단축키
//ctrl+w: Extend Selection
//ctrl+shift+w: shirnk selection
//Alt+up/down

//한꺼번에 여러 줄로
//주석을 쓰는 경우
//Ctrl+/ 를 씁니다

//Ctrl+Alt+l: 코드 리포멧팅
//Shift+Ctrl+Alt+T: 리팩토링 메뉴
//Shift+Ctrl+A: 명령어 검색기능


public class HelloWorld implements Runnable{
    public static void main(String[] args) {
        String helloWorld = "Hello World";
        System.out.println(helloWorld);

        //Alt+Enter로 빠르게 import
        List<Integer> list = new ArrayList<>();


    }

    //Alt+Enter로 오버라이딩 메소드 빠르게 생성
    @Override
    public void run() {

    }

}
