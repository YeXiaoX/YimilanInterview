package com.hx.test.lesson2;

/**
 * Created by yimilan on 2015/11/27.
 */
public class Child extends Parent {

    @Override
    public void doSome(String s) {
        super.doSome(s);
    }

    public static void main(String[] args){
        Parent parent = new Parent();
        parent.serve("parent");
        Child child = new Child();
        child.serve("child");
    }
}
