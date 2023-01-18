package com.programmers.java.baseball.engine;

import com.programmers.java.baseball.Model.Numbers;


//JavaFaker를 통해 숫자를 생성
//핵심 엔진에는 dependency가 없는 것이 좋기때문에 NumberGenerator를 인터페이스로 만들어준다.
public interface NumberGenerator {
    Numbers generate(int count);
}
