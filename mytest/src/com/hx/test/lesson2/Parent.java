package com.hx.test.lesson2;

/**
 * Created by yimilan on 2015/11/27.
 */
public class Parent {

    public void serve(String s){
        this.doSome(s);
    }

    public void doSome(String s){
        System.out.println(s);
    }
}
