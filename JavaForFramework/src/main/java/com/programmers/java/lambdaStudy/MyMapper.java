package com.programmers.java.lambdaStudy;

@FunctionalInterface
public interface MyMapper<IN, OUT> {
    OUT map(IN s);
}
