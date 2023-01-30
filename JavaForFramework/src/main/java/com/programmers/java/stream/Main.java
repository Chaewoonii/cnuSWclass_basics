package com.programmers.java.stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        /*
        * MyCollction에서는 데이터 덩어리를 전달. map의 결과가 한 번에 만들어져서 전달되고, 그 덩어리가 또 filter로 변환되어
        * 또 새로운 MyCollection덩어리가 만들어졌고, 그 덩어리로 forEach구문 수행
        *
        * Stream은 데이터를 한 건씩 처리하기 때문에 한 데이터에 대하여 map, filter, forEach를 수행하고
        * 필요하지 않으면 수행하지 않는 등 하나의 데이터에 대하여 동작을 한다.
        * */
        Arrays.asList("A", "AB", "ABC", "ABCD", "ABCDE")
                .stream()
                .map(String::length)
                .filter(i -> i%2 == 1)
                .forEach(System.out::println);

    }
}
