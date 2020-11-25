package com.luffy.lib.proxy.staticdemo;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/13 14:13<p>
 * 描述：
 */
public class Main {
    public static void main(String[] args) {
        /**
         * 静态代理
         */
        //场景二：客户在美团APP点了一份牛肉炒刀削，此时需要外卖小哥取餐送餐，我们先用静态代理实现
        //方式一：使用类继承
        Customer customerB = new DeliveryMan();//创建一个顾客对象,只不过这次客户给自己找了一个替身
        customerB.order("牛肉炒刀削");
        customerB.stir("牛肉炒刀削");
        //方式二：使用接口实现
        Customer costomerC = new Customer();
        DeliveryManImpl deliveryMan = new DeliveryManImpl(costomerC);
        deliveryMan.order("牛肉炒刀削");
        deliveryMan.stir("牛肉炒刀削");
    }
}
