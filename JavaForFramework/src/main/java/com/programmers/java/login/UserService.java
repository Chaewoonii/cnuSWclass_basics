package com.programmers.java.login;

public class UserService implements Login{

    /*
        login을 이용해서 기능을 수행하기 때문에 "Login에 의존한다"라고 표현
        Login login = new KakaoLogin()이면 기능이 KakaoLogin에 한정됨
        의존성을 외부에 맏긴다면 여러가지 기능을 수행할 수 있는 잠재력을 갖추게 됨
        >> 이를 의존도를 낮춘다고 표현
        의존성을 외부로부터 전달받았다 => 의존성을 주입받았다.
        *의존성 주입, Dependency Injection, DI라고 함
        *Dependency Inversion: 의존성 역전
        *DIP: Dependency Inversion Principle

        결합성: 클래스간 관계가 맺어지는 정도
        KakaoLogin, NaverLoing 등 구상체와 결합하는 경우: 결합이 강하다.
        Login과 같은 추상체와 결합하는 경우: 결합도가 낮아졌다.
     */
    private Login login;

    //Login의 type를 외부에서 받음
    public UserService(Login login){
        this.login = login;
    }

    @Override
    public void login() {
        login.login();
    }

    void run(LoginType loginType) {
        Login user = getLogin(loginType);
        user.login();
    }

    private static Login getLogin(LoginType type) {
        if(type == LoginType.Kakao) return new KakaoLogin();
        return new NaverLogin();

    }
}
