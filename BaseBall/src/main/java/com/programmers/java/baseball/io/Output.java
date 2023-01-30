package com.programmers.java.baseball.io;

import com.programmers.java.baseball.Model.BallCount;

public interface Output {

    void inputError();

    void ballCount(BallCount bc);

    void correct();
}
