package com.xml.ioc.aop.cgli;

public class UserServiceImpl  {

    public String doSomething0(String param) {
        System.out.println("==============doSomething0");
        return "doSomething0";
    }

    public String doSomething1(String param) {
        System.out.println("==============doSomething1");
        return "doSomething1";
    }

    public String doSomething2(String param) {
        System.out.println("==============doSomething2");
        return "doSomething2";
    }

    public String myMethod(String param) {
        System.out.println("==============myMethod");
        return "myMethod";
    }
}
