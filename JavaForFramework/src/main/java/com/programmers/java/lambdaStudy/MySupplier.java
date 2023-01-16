package com.programmers.java.lambdaStudy;

@FunctionalInterface
public interface MySupplier<T> {
    T supply();
}
