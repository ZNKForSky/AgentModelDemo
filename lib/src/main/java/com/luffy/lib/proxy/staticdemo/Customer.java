package com.luffy.lib.proxy.staticdemo;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/10 11:44<p>
 * 描述：顾客 ------ 充当代理模式中的真实对象
 */
public class Customer implements OrderService {
    @Override
    public String order(String foodName) {
        System.out.println("我点了一份" + foodName);
        return foodName;
    }

    @Override
    public void stir(String foodName) {
        System.out.println("快递小哥能帮我搅拌一下" + foodName + "吗？");
    }

    public void eat(String foodName) {
        System.out.println("我在吃" + foodName);
    }
}
