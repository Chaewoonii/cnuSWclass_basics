package com.programmers.java.baseball;

import com.programmers.java.baseball.Model.BallCount;
import com.programmers.java.baseball.Model.Numbers;
import com.programmers.java.baseball.engine.BaseBall;
import com.programmers.java.baseball.io.Input;
import com.programmers.java.baseball.io.NumberGenerator;
import com.programmers.java.baseball.io.Output;

public class App {
    public static void main(String[] args) {

        //NumberGenerator generator = new FakerNumberGenerator();
        NumberGenerator generator = new HackFakerNumberGenerator();
        Console console = new Console();


        new BaseBall(generator, console, console).run();

    }
}
