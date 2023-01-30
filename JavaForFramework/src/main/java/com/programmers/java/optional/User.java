package com.programmers.java.optional;

public class User {

    public static User EMPTY = new User(0, "");
    private int age;
    private String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public boolean isOver19(){
        if (this == EMPTY) return false;
        return age >= 19;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return name + "(" + age + ")";
    }

    public String getName() {
        return name;
    }
}
