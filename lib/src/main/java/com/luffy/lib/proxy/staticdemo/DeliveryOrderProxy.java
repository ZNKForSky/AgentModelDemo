package com.luffy.lib.proxy.staticdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/12 16:37<p>
 * 描述：模拟Proxy.newProxyInstance(ClassLoader loader,
 *                                 Class<?>[] interfaces,
 *                                 InvocationHandler h)方法中做的事情，从底层看一看到底JDK如何完成的动态代理
 *
 *  总结：JDK静态代理是通过直接编码创建的，而JDK动态代理是利用反射机制在运行时创建代理类的。其实在动态代理中，核心
 *  是InvocationHandler。每一个代理的实例都会有一个关联的调用处理程序(InvocationHandler)。对待代理实例进行调用时，
 *  将对方法的调用进行编码并指派到它的调用处理器(InvocationHandler)的invoke方法。所以对代理对象实例方法的调用都是
 *  通过InvocationHandler中的invoke方法来完成的，而invoke方法会根据传入的代理对象、方法名称以及参数决定调用代理的
 *  哪个方法。
 *
 */
public class DeliveryOrderProxy implements OrderService {
    private final InvocationHandler handler;

    public DeliveryOrderProxy(InvocationHandler handler) {
        this.handler = handler;
    }

    /**
     *
     * @param foodName 食物名称
     * @return
     */
    @Override
    public String order(String foodName) {
        try {
            Method method = OrderService.class.getMethod("order", String.class);
            //每个方法的实现，实际上并没有做其他的事情，而是直接调用了InvocationHandler中的invoke方法
            handler.invoke(this,method,new Object[]{foodName});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return foodName+"已经送达，请尽快取餐！";
    }

    /**
     *
     * @param foodName 食物名称
     */
    @Override
    public void stir(String foodName) {
        try {
            Method method = OrderService.class.getMethod("stir", String.class);
            //每个方法的实现，实际上并没有做其他的事情，而是直接调用了InvocationHandler中的invoke方法
            handler.invoke(this,method,new Object[]{foodName});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
