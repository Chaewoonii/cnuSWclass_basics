package com.programmers.java.defaultMethod2;

import com.programmers.java.defaultMethod.MyInterfaceDefault;

public class Main {
    public static void main(String[] args) {
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();

        //익명 클래스: 인터페이스를
        new MyAblility(){
            @Override
            public void run(){
                System.out.println("RUN");
            }
        }.run();
    }
}

interface Flyable{
    default void fly(){
        System.out.println("FLY");
    };
}

interface Swimmable{
    default void swim(){
        System.out.println("SWIM");
    };
}

interface Walkable{
    default void walk(){
        System.out.println("WALK");
    };
}

interface Ability{
    static void sayHello(){
        System.out.println("Hello");
    }
}

@FunctionalInterface
interface MyAblility{
    void run();
    default void walk(){
        System.out.println("WALKING");
    }

    default void swim(){
        System.out.println("SWIMMING");
    }

}

class Duck implements Swimmable, Walkable{
}

class Swan implements Flyable, Swimmable, Walkable{
}