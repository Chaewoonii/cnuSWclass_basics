package com.programmers.java.login;

public class Main {
    public static void main(String[] args) {
        UserService s = new UserService(new KakaoLogin());
        s.login();
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
