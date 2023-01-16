package com.programmers.java.lambdaStudy;

@FunctionalInterface
public interface MyConsumer<T> {
    void consume(T t);
}
