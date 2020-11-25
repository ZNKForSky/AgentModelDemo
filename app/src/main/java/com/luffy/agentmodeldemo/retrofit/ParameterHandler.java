package com.luffy.agentmodeldemo.retrofit;

/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/25 15:11<p>
 * 描述：处理请求参数
 */
public abstract class ParameterHandler {
    abstract void apply(ServiceMethod serviceMethod, String value);

    static class FieldParameterHandler extends ParameterHandler {
        private String mKey;

        public FieldParameterHandler(String key) {
            mKey = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParameter(mKey, value);
        }
    }

    static class QueryParameterHandler extends ParameterHandler {
        private String mKey;

        public QueryParameterHandler(String key) {
            mKey = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addFieldParameter(mKey, value);

        }
    }
}
