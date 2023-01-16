package com.programmers.java.defaultMethod;

public class Main {
}

class Service implements MyInterface{

    @Override
    public void method1() {

    }

    //method2를 쓰지 않음에도 method2를 만들어야 함
    @Override
    public void method2() {

    }
}


class MyService extends MyInterfaceAdapter{
    @Override
    public void method1(){
        System.out.println("Hello! Myservice extends MyInterfaceAdapter");
    }
}

class MyService2 extends Object implements MyInterface{

    @Override
    public void method1() {
        System.out.println("MyService2의 method1입니다");
    }

    @Override
    public void method2() {
        //nothing
    }
}

class MyService3 implements MyInterfaceDefault{

    //추상 메소드 구현
    @Override
    public void method1(){
        System.out.println("MyService3의 method1입니다");
    }

    //default: 원하는 메소드만 구현 가능
    @Override
    public void method3(){
        System.out.println("MyService3의 method3입니다");
    }
}