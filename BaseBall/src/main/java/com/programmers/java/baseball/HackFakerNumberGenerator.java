package com.programmers.java.baseball;

import com.github.javafaker.Faker;
import com.programmers.java.baseball.Model.Numbers;
import com.programmers.java.baseball.io.NumberGenerator;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.stream.Stream;

@AllArgsConstructor
public class HackFakerNumberGenerator implements NumberGenerator {
    private final Faker faker = new Faker();
    @Override
    public Numbers generate(int count) {
        Faker faker = new Faker();

        Numbers nums = new Numbers(
                Stream.generate(() -> faker.number().randomDigitNotZero())
                        .distinct()
                        .limit(count)
                        .toArray(Integer[]::new)
        );
        System.out.println(nums);
        return nums;
    }
}
