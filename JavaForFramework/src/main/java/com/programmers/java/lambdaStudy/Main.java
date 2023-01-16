package com.programmers.java.lambdaStudy;

public class Main {
    public static void main(String[] args) {

        MySupplier<String> s = () -> "Hello world";
        MyMapper<String, Integer> m = String::length;
        MyMapper<Integer, Integer> m2 = i -> i * i;
        MyMapper<Integer, String> m3 = Integer::toHexString; //i -> Integer.toHexString(i)
        MyConsumer<String> c = System.out::println;

        /*
        c.consume( // String, 출력
                m3.map( // Integer -> String
                        m2.map( //Integer -> Integer
                                m.map( //String -> Integer
                                        s.supply()//String
                                )
                        )
                )
        );
        */

        MyRunnable r = () -> c.consume(m3.map(m2.map(m.map(s.supply()))));
        r.run();
    }
}
