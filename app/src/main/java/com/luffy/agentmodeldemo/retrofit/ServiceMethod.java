package com.luffy.agentmodeldemo.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/25 10:18<p>
 * 描述：记录解析注解过程中的信息，比如请求方式（POST or GET）、完整接口地址等。
 */
class ServiceMethod {

    private static Annotation[] annotations;
    private static Annotation[][] parameterAnnotations;
    private static ParameterHandler[] parameterHandlers;
    private Call.Factory mCallFatory;
    //请求方式
    private static String httpMethod;
    //标记是否有请求体，默认没有
    private static boolean hasBody;
    //让ServiceMethod持有MyRetrofit的一个实例
    private MyRetrofit myRetrofit;
    //请求的基础URL
    private HttpUrl baseUrl;
    //完整的请求地址
    private HttpUrl finalUrl;
    //路由
    private static String router;
    //POST请求的请求体
    private RequestBody formBody;
    //GET请求需要把参数拼接到url里面
    private HttpUrl.Builder urlBuilder;
    //okhttp的请求对象
    private FormBody.Builder formBuild;


    public ServiceMethod(MyRetrofit myRetrofit) {
        this.myRetrofit = myRetrofit;
        mCallFatory = myRetrofit.mCallFatory;
        baseUrl = myRetrofit.mBaseUrl;
        //如果是有请求体,创建一个okhttp的请求体对象
        if (hasBody) {
            formBuild = new FormBody.Builder();
        }
    }

    /**
     * 在这里真正地解析注解
     *
     * @param myRetrofit
     * @param method
     * @return
     */
    public static ServiceMethod parseAnnotations(MyRetrofit myRetrofit, Method method) {
        //1.解析方法上的注解。
        annotations = method.getAnnotations();
        //2.解析方法参数的注解 ------ 一个方法可能有多个参数，一个参数可能有多个注解，所以返回的是二维数组。
        parameterAnnotations = method.getParameterAnnotations();

        //3.遍历方法上的注解，这里只处理POST和GET请求
        for (Annotation annotation : annotations) {
            if (annotation instanceof POST) {
                httpMethod = "POST";
                hasBody = true;
                router = ((POST) annotation).value();
            } else if (annotation instanceof GET) {
                httpMethod = "GET";
                hasBody = false;
                router = ((GET) annotation).value();
            }
        }

        //4.遍历方法参数的注解
        int length = parameterAnnotations.length;
        parameterHandlers = new ParameterHandler[length];
        for (int i = 0; i < length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                //4.1 我们规定POST请求的方法参数必须用Field标记，否则抛异常提示用户
                if ("POST".equals(httpMethod)) {
                    if (annotation instanceof Field) {
                        //4.2 获取参数的key
                        String key = ((Field) annotation).value();
                        parameterHandlers[i] = new ParameterHandler.FieldParameterHandler(key);
                    } else {
                        throw new IllegalStateException("You must use @Field to mark the method parameters of the POST request");
                    }
                } else if ("GET".equals(httpMethod)) {
                    if (annotation instanceof Query) {
                        String key = ((Query) annotation).value();
                        parameterHandlers[i] = new ParameterHandler.QueryParameterHandler(key);
                    } else {
                        throw new IllegalStateException("You must use @Query to mark the method parameters of the GET request");
                    }
                }
            }
        }
        return new ServiceMethod(myRetrofit);
    }

    /**
     * GET请求，把 k-v 拼接到url后面
     *
     * @param key   键
     * @param value 值
     */
    public void addQueryParameter(String key, String value) {
        //1.GET请求第一次添加参数到url中的时候，urlBuild肯定是空;
        if (urlBuilder == null) {
            //2.此时把baseUrl和router(路由)进行拼接;
            urlBuilder = baseUrl.newBuilder(router);
        }
        //3.再依次添加所有参数到url中。
        finalUrl = urlBuilder.addQueryParameter(key, value).build();
    }

    /**
     * POST请求，把 k-v 拼接到url后面
     *
     * @param key   键
     * @param value 值
     */
    public void addFieldParameter(String key, String value) {
        formBuild.add(key, value);
    }

    /**
     * 处理网络请求
     *
     * @param args 实参，即key对应的值
     * @return
     */
    public Object invoke(Object[] args) {
        //1.处理请求地址和参数
        for (int i = 0; i < parameterHandlers.length; i++) {
            ParameterHandler parameterHandler = parameterHandlers[i];
            //1.1ParameterHandler中存着参数的key，现在把对应的value传过去。 ps:参数的k-v是一一对应的，所以parameterHandlers[i]与args[i]也是一一对应的。
            parameterHandler.apply(this, args[i].toString());
        }
        //2.如果是POST请求，势必urlBuild为空
        if (urlBuilder == null) {
            //2.1 此时需要将baseUrl和router(路由)拼接
            finalUrl = baseUrl.newBuilder(router).build();
            formBody = formBuild.build();
        }
        Request request = new Request.Builder().url(finalUrl).method(httpMethod, formBody).build();
        return mCallFatory.newCall(request);
    }
}
