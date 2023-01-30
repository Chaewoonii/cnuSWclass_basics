package com.programmers.java.iterator;

import com.programmers.java.collectionsStudy.MyCollection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyIterator<String> iter =
                new MyCollection<String>(Arrays.asList("A", "AAA", "ABCD", "CCCC", "DEDEF"))
                        .iterator();
        //아래의 코드를 위의 코드로 변경
        //List<String> list = Arrays.asList("A", "AAA", "ABCD", "CCCC", "DEDEF");
        //Iterator<String> iter = list.iterator();

        /*
        //next할 때마다 다음 데이터로 이어짐
        //이전 데이터를 조회할 수 없음

        iter.next();
        iter.next();
        iter.next();
        System.out.println(iter.next());
        iter.next();
        //iterator가 가진 데이터의 길이를 넘어서면 에러 발생. 데이터가 없는데 왜 계속 호출?
        //hasNext: 다음 데이터가 있는지 확인
        if(iter.hasNext()) iter.next(); //이미 5개의 데이터를 꺼냈기 때문에 false
        */

        //전체 데이터 모두 순회
        while(iter.hasNext()){
            String s = iter.next();
            int len = s.length();
            if(len % 2 == 0) continue;;
            System.out.println(iter.next());
        }

    }
}
