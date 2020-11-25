package com.luffy.lib.proxy.dynamic.cglib;

import com.luffy.lib.proxy.staticdemo.Customer;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/13 14:20<p>
 * 描述：CGLib实现动态代理（基于类继承实现）
 * 基于父类的动态代理，是在内存中生成了一个对象，该对象继承了原对象（目标类对象）。所以代理类对象实际上是目标类对象的儿子。
 */
public class Main {
    public static void main(String[] args) {
        //1。创建真实对象
        Customer customerE = new Customer();
        //2.使用一个Enhancer静态方法create创建代理对象。它不要求目标类实现接口，但是要求目标类不能是最终类，也就是不能被final修饰。
        //​ 因为CGLIB是基于目标类生成该类的一个子类作为代理类，所以目标类必须可被继承。
        /**
         * @param type class to extend or interface to implement 指定我们要代理的目标类的字节码对象，也就是指定目标类的类型
         * @param callback the callback to use for all methods 由于该接口只是一个名称定义的作用，并不包含方法的声明。所以我们使用时通常使用它的一个子接口MethodInterceptor
         */
        Customer customerF = (Customer) Enhancer.create(customerE.getClass(), (MethodInterceptor)
                /**
                 * @parame obj 就是代理类对象的一个引用，也就是Enhancer.create的返回值，此引用几乎不回用到，忽略即可。
                 * @parame method 对应的是触发intercept执行的方法的Method对象。假如我们调用了xxx方法，该方法触发了intercept的执行，那么，method就是xxx方法对应的反射对象（Method对象）
                 * @parame args1 代理对象调用方法时，传递的实际参数
                 * @parame proxy 方法的代理对象，一般也不作处理，可以暂时忽略
                 */
                (obj, method, args1, proxy) -> {
                    method.invoke(customerE, args1);
                    System.out.println("您下单了一份" + args1[0]);
                    System.out.println("正在取餐途中...");
                    System.out.println("已取餐，正在配送途中...");
                    return "因路途颠簸，被迫帮您把" + args1[0] + "搅拌均匀，请放心使用！";
                });
        String order = customerF.order("西湖牛肉羹");
        System.out.println("order ====== " + order);
    }
}
