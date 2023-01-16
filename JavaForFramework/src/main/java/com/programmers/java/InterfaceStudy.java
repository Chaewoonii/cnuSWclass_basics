package com.programmers.java;

interface Runnable{
    void run();
}
interface MyRunnable{
    void myRun();
}

public class InterfaceStudy implements Runnable, MyRunnable{

    @Override
    public void run() {
        System.out.println("Runnable run()");
    }

    @Override
    public void myRun() {
        System.out.println("MyRunnable myRun()");

    }
}
