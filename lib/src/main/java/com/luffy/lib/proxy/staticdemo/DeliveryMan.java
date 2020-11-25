package com.luffy.lib.proxy.staticdemo;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/10 13:26<p>
 * 描述：外卖小哥 ------ 充当代理模式中的代理   以类继承的形式实现
 */
public class DeliveryMan extends Customer {
    @Override
    public String order(String foodName) {
        super.order(foodName);
        System.out.println("您下单了一份" + foodName);
        System.out.println("正在取餐途中...");
        System.out.println("已取餐，正在配送途中...");
        return foodName;
    }

    @Override
    public void stir(String foodName) {
        super.stir(foodName);
        System.out.println("当然可以，职责所在！");
        System.out.println("已经帮您搅拌好了，您可以用餐了！");
    }
}
