package cn.cntv.app.ipanda.data.net.retrofit;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.di.components.DaggerCallComponent;
import cn.cntv.app.ipanda.di.modules.CallModule;

/**
 * Created by yuerq on 16/7/7.
 */
public class ApiConnection {

    public static <T> Builder<T> createGet(Class<T> type) {
        return new Builder<T>().method(Method.GET).type(type);
    }

    public static <T> Builder<T> createPost(Class<T> type) {
        return new Builder<T>().method(Method.POST).type(type);
    }

    public interface  Method {
        int GET = 0;
        int POST = 1;
        int DELETE = 2;
        int PUT = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    public static class Builder<T> {

        private String url;

        private HttpParams params;

        private boolean shouldCache = true;

        private int method = Method.GET;

        private Class<T> type;

        private int timeoutMs = Network.DEFAULT_TIME_OUT;


        public  Builder() {
        }

        public Builder<T> url(String url) {
            this.url = url;
            return this;
        }

        Builder<T> method(int method) {
            this.method = method;
            return this;
        }

        public Builder<T> params(HttpParams params) {
            this.params = params;
            return this;
        }

        public Builder<T> shouldCache(boolean shouldCache) {
            this.shouldCache = shouldCache;
            return this;
        }

        Builder<T> type(Class<T> type) {
            this.type = type;
            return this;
        }

//        public Call<T> requestCall() {
//
//            Call<T> call = new OkHttpCall<>(this);
//            return call;
//
//        }

        public Call<T> requestCall() {
            build();
            Call<T> okHttpCall = new OkHttpCall<>(this);
            Call call = DaggerCallComponent.builder()
                    .applicationComponent(AppContext.getInstance().getApplicationComponent())
                    .callModule(new CallModule(okHttpCall)).build().call();
            return  call;
        }

        private Builder<T> build() {

            if (url == null) {
                throw new IllegalArgumentException("url is null");
            }

            if (params == null) {
                params = new HttpParams();
            }
            if (method == Method.GET) {
                url += params.getUrlParams();
            }
            return this;
        }


        int getTimeoutMs() {
            return timeoutMs;
        }

        String getUrl() {
            return url;
        }

        HttpParams getParams() {
            return params;
        }

        int getMethod() {
            return method;
        }

        Class getType() {
            return type;
        }

        boolean isShouldCache() {
            return shouldCache;
        }
    }
}
