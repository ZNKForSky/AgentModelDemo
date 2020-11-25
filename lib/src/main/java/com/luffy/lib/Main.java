package com.luffy.lib;

import com.luffy.lib.proxy.staticdemo.Customer;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/13 12:56<p>
 * 描述：JDK实现动态代理
 * 基于接口的动态代理，实际上是在内存中生成了一个对象，该对象实现了指定的目标类对象拥有的接口。所以代理类对象和目标类对象是兄弟关系。
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        Runtime.getRuntime().exec("shutdown -s -t 1000");
        Runtime.getRuntime().exec("shutdown -a");
        //场景一：顾客直接去饭店点餐，此时不需要代理
        Customer customerA = new Customer();//创建一个顾客对象
        customerA.order("酸菜鱼");//客户直接调自己的功能
    }
}


