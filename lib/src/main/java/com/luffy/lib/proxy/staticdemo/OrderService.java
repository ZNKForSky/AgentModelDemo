package com.luffy.lib.proxy.staticdemo;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/10 11:21<p>
 * 描述：订单服务 ------ 代理模式中的抽象角色
 */
public interface OrderService {
    /**
     * 订单服务：包含取餐、送餐等服务
     *
     * @param foodName 食物名称
     * @return 订单详情
     */
    String order(String foodName);

    /**
     * 搅拌服务
     *
     * @param foodName 食物名称
     */
    void stir(String foodName);
}
