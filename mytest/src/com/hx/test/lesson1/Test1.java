package com.hx.test.lesson1;

import sun.misc.Lock;

/**
 * Created by yimilan on 2015/11/14.
 */
public class Test1 {

    public Lock lock;

    public Test1(Lock _lock){
        lock = _lock;
    }

    public synchronized void write(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : write start .....");
        try {
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : write end .....");
    }

    public synchronized void read(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : read start .....");
        try {
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : read end .....");
    }

    public void write2(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : write start .....");
        try {
            lock.lock();
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : write end .....");
    }

    public void read2(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : read start .....");
        try {
            lock.lock();
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : read end .....");
    }

    public static void main(String[] args){
        Test1 test1 = new Test1(new Lock());
        TestThread1 testThread1 = new TestThread1(test1);
        TestThread2 testThread2 = new TestThread2(test1);
        testThread1.start();
        testThread2.start();
    }

    static class TestThread1 extends Thread{
        private Test1 test1;
        public TestThread1(Test1 _test1){
            test1 = _test1;
        }

        public void run(){
            test1.write2();
        }
    }

    static class TestThread2 extends Thread{
        private Test1 test1;
        public TestThread2(Test1 _test1){
            test1 = _test1;
        }

        public void run(){
            test1.read2();
        }
    }
}
