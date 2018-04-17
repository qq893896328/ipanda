package cn.cntv.app.ipanda.data.net;


import cn.cntv.app.ipanda.data.net.retrofit.ApiConnection;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.HttpParams;

/**
 * Created by yuerq on 16/7/8.
 */
public class HttpTools {
    public static <T> Call<T> get(String url, Class<T> type, HttpParams params) {

        return  ApiConnection.createGet(type)
                .url(url)
                .params(params)
                .requestCall();
    }

    public static <T> Call<T> get(String url, Class<T> type, boolean shouldCache, HttpParams params) {

        return   ApiConnection.createGet(type)
                .url(url)
                .params(params)
                .shouldCache(shouldCache)
                .requestCall();
    }


    public static <T> Call<T> post(String url, Class<T> type, HttpParams params) {

        return ApiConnection.createPost(type)
                .url(url)
                .params(params)
                .requestCall();
    }

}
