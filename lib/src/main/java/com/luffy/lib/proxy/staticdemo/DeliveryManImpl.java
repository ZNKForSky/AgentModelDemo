package com.luffy.lib.proxy.staticdemo;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/10 10:58<p>
 * 描述：外卖小哥 ------ 充当代理模式中的代理   以接口的形式实现
 */
public class DeliveryManImpl implements OrderService {
    // 把原来的对象传入，并报存到成员位置。也就是目标类对象
    private OrderService source;

    public DeliveryManImpl(OrderService source) {
        this.source = source;
    }

    @Override
    public String order(String foodName) {
        source.order(foodName);
        System.out.println("您下单了一份" + foodName);
        System.out.println("正在取餐途中...");
        System.out.println("已取餐，正在配送途中...");
        return foodName;
    }

    @Override
    public void stir(String foodName) {
        source.stir(foodName);
        System.out.println("当然可以，职责所在！");
        System.out.println("已经帮您搅拌好了，您可以用餐了！");
    }
}
