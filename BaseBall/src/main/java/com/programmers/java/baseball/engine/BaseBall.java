package com.programmers.java.baseball.engine;

import com.programmers.java.baseball.Model.BallCount;
import com.programmers.java.baseball.Model.Numbers;
import com.programmers.java.baseball.io.Input;
import com.programmers.java.baseball.io.NumberGenerator;
import com.programmers.java.baseball.io.Output;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

//BaseBall 클래스는 NumberGenerator, Input, Output 객체를 생성자를 통해 주입받고 있다
//객체의 의존성을 낮춤
//핵심 비즈니스 로직은 외부 Dependency가 없도록 설계하는 것이 좋음
//Dependency를 가져가는 순간 외부에 의해 수정될 여지가 남기 때문
@AllArgsConstructor
public class BaseBall implements Runnable{

    private final int COUNT_OF_NUMBERS = 3;
    private NumberGenerator generator;
    private Input input;
    private Output output;


    @Override
    public void run() {
        Numbers answer = generator.generate(COUNT_OF_NUMBERS);

        while(true){
            String inputString = input.input("숫자를 맞춰보세요: ");
            Optional<Numbers> inputNumbers = parse(inputString);
            if (inputNumbers.isEmpty()){
                output.inputError();
                continue;
            }

            BallCount bc = ballCount(answer, inputNumbers.get());
            output.ballCount(bc);
            if(bc.getStrike() == COUNT_OF_NUMBERS){
                output.correct();
                break;
            }


        }
    }

    private BallCount ballCount(Numbers answer, Numbers inputNumbers) {
        AtomicInteger strike = new AtomicInteger(); // 동기화 기능을 추가, 인텔리제이가 추가해줌
        AtomicInteger ball = new AtomicInteger();
        //AtomicInteger: 동기화가 완료된 Integer

        answer.indexedForEach((a, i) ->{
            inputNumbers.indexedForEach((n, j) -> {

                //strike, ball: 가져와서 읽을 수는 있는데 쓰는 것(초기화)는 제한된다
                //멀티 쓰레드 환경에서 여러 쓰레드에서 변수를 가져오면 Race condition(경쟁상태)이 일어나게 된다.
                //Race Condition(경쟁 상태): 둘 이상의 입력 또는 조작의 타이밍이나 순서 등이 결과값에 영향을 줄 수 있는 상태
                //밖으로 초기화되어야한다면, 밖의 변수에 동기화 기능을 추가해야 한다
                if (!a.equals(n)) return;

                //addAndGet: strike에 1을 증가하고 가져와라. // getAndAdd: 값을 가져오고 1을 증가해라.
                if(i.equals(j)) strike.addAndGet(1);
                else ball.addAndGet(1);
            });
        });
        return new BallCount(strike.get(), ball.get());
    }

    private Optional<Numbers> parse(String inputString) {
        if (inputString.length() != COUNT_OF_NUMBERS) return Optional.empty();

        //IntStream. chars는 스트림임.
        long count = inputString.chars()
                .filter(Character::isDigit)
                .map(Character::getNumericValue)
                .filter(i -> i>0)
                .distinct()
                .count();

        if(count != COUNT_OF_NUMBERS) return Optional.empty();

        return Optional.of(
                new Numbers(
                        inputString.chars()
                                .map(Character::getNumericValue)
                                .boxed()
                                .toArray(Integer[]::new)
                )
        );
    }
}
