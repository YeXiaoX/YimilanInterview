package com.hx.test.lesson1;

/**
 * Created by yimilan on 2015/11/14.
 */
public class Test2 {

    public int c = 0;
    public Integer integer = 0;

    public void add() {
        c++;
        integer++;
    }

    public static void main(String[] args) {
        final int i = 0;
        Test2 test2 = new Test2();
        new Thread(() -> {
            test2.add();
            Test2 test3 = new Test2();
            System.out.println("thread1 " + i);
            System.out.println("thread1 " + test2.c + " " + test2.integer);
            new Thread(() -> {
                test3.add();
                System.out.println("thread2 " + test2.c + " " + test2.integer);
            }).start();
            System.out.println("thread1 " + test2.c + " " + test2.integer);
        }).start();
    }
}
