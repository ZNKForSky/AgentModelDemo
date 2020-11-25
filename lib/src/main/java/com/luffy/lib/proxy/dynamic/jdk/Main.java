package com.luffy.lib.proxy.dynamic.jdk;

import com.luffy.lib.proxy.staticdemo.Customer;
import com.luffy.lib.proxy.staticdemo.DeliveryOrderProxy;
import com.luffy.lib.proxy.staticdemo.OrderService;

import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/13 14:13<p>
 * 描述：JDK实现动态代理
 * 基于接口的动态代理，实际上是在内存中生成了一个对象，该对象实现了指定的目标类对象拥有的接口。所以代理类对象和目标类对象是兄弟关系。
 */
public class Main {
    public static void main(String[] args) throws Exception {
        /**
         *  动态代理
         *
         *   JDK自带的动态代理技术，需要使用一个静态方法来创建代理对象。
         *   它要求被代理对象，也就是目标类，必须实现接口。
         * ​ 生成的代理对象和原对象都实现相同的接口，是兄弟关系。
         */
        //1.准备一个真实对象，也就是我们的顾客对象
        final Customer customerD = new Customer();
        //2.使用JDK的API，动态生成一个代理对象
        OrderService orderService = (OrderService) Proxy.newProxyInstance(
                Main.class.getClassLoader(),//第一个参数是类加载器，可以是测试类的加载器，也可以是真实对象的加载器
                customerD.getClass().getInterfaces(),//第二个参数是被代理对象实现的所有的接口的字节码数组
                /**
                 * @param proxy 代理类对象的一个引用，也就是Proxy.newProxyInstance的返回值，此引用几乎不回用到，忽略即可。
                 * @param method 对应的是触发invoke执行的方法的Method对象。假如我们调用了xxx方法，该方法触发了invoke的执行，那么，method就是xxx方法对应的反射对象（Method对象）
                 * @param args1 代理对象调用方法时，传递的实际参数
                 */
                (proxy, method, args1) -> {//执行处理器 用于定义方法的增强规则（加强后的方法）
                    //下面打印语句会报错，因为打印proxy就相当于调用了proxy.toString()，继而转发到InvocationHandler的invoke()中，而invoke中又调用了proxy.toString()，导致死循环，最终StackOverflowError
//                    System.out.println("proxy ====== "+proxy);
                    Class<?> declaringClass = method.getDeclaringClass();
                    String name = declaringClass.getName();
                    String simpleName = declaringClass.getSimpleName();
                    System.out.println("name === " + name + ",simpleName === " + simpleName);
                    if ("order".equals(method.getName())) {
                        method.invoke(customerD, args1);
                        System.out.println("您下单了一份" + args1[0]);
                        System.out.println("正在取餐途中...");
                        System.out.println("已取餐，正在配送途中...");
                        return "因路途颠簸，被迫帮您把" + args1[0] + "搅拌均匀，请放心使用！";
                    } else {
                        method.invoke(customerD, args1);
                        System.out.println("当然可以，职责所在！");
                        System.out.println("已经帮您搅拌好了，您可以用餐了！");
                        return null;
                    }

                }
        );
        String order = orderService.order("飞天茅台");
        System.out.println("订单详情 ====== " + order);
        orderService.stir("麻婆豆腐");

        /**
         * 测试模拟JDK动态代理机制的DEMO
         */
        OrderService orderServiceTest = new DeliveryOrderProxy((proxy, method, args2) -> {
            //不同方法增强的方法不一样，通过方法名进行不同的逻辑处理
            if ("order".equals(method.getName())) {
                method.invoke(customerD, args2);
                System.out.println("您下单了一份" + args2[0]);
                System.out.println("正在取餐途中...");
                System.out.println("已取餐，正在配送途中...");
                return "因路途颠簸，被迫帮您把" + args2[0] + "搅拌均匀，请放心使用！";
            } else {
                method.invoke(customerD, args2);
                System.out.println("当然可以，职责所在！");
                System.out.println("已经帮您搅拌好了，您可以用餐了！");
                return null;
            }
        });

        String orderTest = orderServiceTest.order("飞天茅台");
        System.out.println("订单详情 ====== " + orderTest);
        orderServiceTest.stir("麻婆豆腐");

        proxy();
    }

    /**
     * 将生产的代理类输出到lib目录下
     *
     * @throws Exception
     */
    private static void proxy() throws Exception {
        String name = Main.class.getName() + "$Proxy0";
        //生成代理指定接口的Class数据
        byte[] bytes = ProxyGenerator.generateProxyClass(name, Customer.class.getInterfaces());
        FileOutputStream fos = new FileOutputStream("lib/" + name + ".class");
        fos.write(bytes);
        fos.close();
    }
}
