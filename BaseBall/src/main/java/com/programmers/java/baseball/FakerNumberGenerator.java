package com.programmers.java.baseball;

import com.github.javafaker.Faker;
import com.programmers.java.baseball.Model.Numbers;
import com.programmers.java.baseball.io.NumberGenerator;

import java.util.stream.Stream;

public class FakerNumberGenerator implements NumberGenerator {
    private final Faker faker = new Faker();
    @Override
    public Numbers generate(int count) {
        Faker faker = new Faker();

        return new Numbers(
                Stream.generate(() -> faker.number().randomDigitNotZero())
                        .distinct()
                        .limit(count)
                        .toArray(Integer[]::new)
        );
    }
}
