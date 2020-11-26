package com.luffy.agentmodeldemo.retrofit;

import android.content.ContentProviderOperation;
import android.support.annotation.Nullable;
import android.util.Log;

import com.luffy.agentmodeldemo.api.MyWeatherApi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/24 15:37<p>
 * 描述：我的Retrofit请求框架
 */
public class MyRetrofit {
    private static final String TAG = "MyRetrofit";
    private final Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();
    //Call.Factory的唯一实现类OkHttpClient
    final Call.Factory mCallFatory;
    protected HttpUrl mBaseUrl;

    public MyRetrofit(Call.Factory callFactory, HttpUrl baseUrl) {
        mCallFatory = callFactory;
        mBaseUrl = baseUrl;
    }

    /**
     * 利用JDK动态代理对API接口进行代理
     *
     * @param service 抽象角色，即被代理的对象
     * @param <T>     被代理对象的泛型类型形参
     * @return 被代理的对象实例
     */
    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(MyRetrofit.class.getClassLoader(), new Class[]{service}, (proxy, method, args) -> {
            //解析Method涉及的注解，包含方法上的注解和参数的注解。为了避免每次调用接口都去解析注解，我们可以对解析注解过程中涉及的一些信息做缓存。
            Log.i(TAG, "invoke: method"+method.getName());
            ServiceMethod serviceMethod = loadServiceMethod(method);
            return serviceMethod.invoke(args);
        });
    }

    /**
     * @param method
     * @return
     */
    ServiceMethod loadServiceMethod(Method method) {
        //1.先从缓存中取，取到了直接返回。
        ServiceMethod result = serviceMethodCache.get(method);
        if (result != null) return result;
        //2.为了避免多线程中重复解析注解，这里进行加锁，并且再次从缓存中获取，如果取不到，再去解析。
        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = ServiceMethod.parseAnnotations(this, method);
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    /**
     * 构建者模式：将一个复杂对象的构建和它的表示分离，可以使调用者不必知道内部组成的细节。
     */
    public static class Builder {
        private @Nullable
        okhttp3.Call.Factory mCallFactory;
        private @Nullable
        HttpUrl mBaseUrl;

        public Builder baseUrl(String baseUrl) {
            mBaseUrl = HttpUrl.get(baseUrl);
            return this;
        }

        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.mCallFactory = Objects.requireNonNull(factory, "factory == null");
            return this;
        }

        public MyRetrofit build() {
            if (mBaseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }

            okhttp3.Call.Factory callFactory = mCallFactory;
            //如果没有手动调用callFactory,那么callFactory肯定是空的，就需要给它一个默认的OkHttpClient
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new MyRetrofit(callFactory, mBaseUrl);
        }
    }
}
