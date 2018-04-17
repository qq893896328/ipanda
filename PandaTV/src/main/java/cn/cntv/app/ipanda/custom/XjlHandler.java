package cn.cntv.app.ipanda.custom;

import android.net.Uri;

import java.util.Map;

import cn.cntv.app.ipanda.data.net.HttpTools;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.HttpParams;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.utils.JsonUtils;

/**
 * @author Xiao JinLai
 * @Date 2015-12-10 12:26
 * @Description 网络请求处理类
 */
public class XjlHandler<T> {

    private static final int ERROR = Integer.MAX_VALUE; // 请求错误数值

    private HandlerListener mListener = null;

    public XjlHandler(HandlerListener listener) {

        mListener = listener;
    }

    /**
     * 获取 String 型网络数据
     *
     * @param url  请求地址
     * @param what 当前请求标识
     */
    public void getHttpString(String url, final int what) {

        HttpTools.get(url, null, null).enqueue(
                new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        HandlerMessage tMessage = new HandlerMessage();
                        tMessage.what = what;
                        tMessage.obj = response.body();

                        mListener.handlerMessage(tMessage);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        HandlerMessage tMessage = new HandlerMessage();
                        tMessage.what = ERROR;
                        tMessage.whatTag = what;
                        mListener.handlerMessage(tMessage);
                    }
                }
        );
    }

    /**
     * 获取 Json 型网络数据
     *
     * @param url  请求地址
     * @param clas 请求实体类
     * @param what 当前请求操作标识
     */
    public <TT> void getHttpJson(String url, final Class<TT> clas, final int what) {

        HttpTools.get(url, clas, null).enqueue(new Callback<TT>() {
            @Override
            public void onResponse(Call<TT> call, Response<TT> response) {
                try {
                    TT t = response.body();
                    HandlerMessage tMessage = new HandlerMessage();
                    tMessage.what = what;
                    tMessage.obj = t;
                    mListener.handlerMessage(tMessage);
                } catch (Exception e) {
                    onFailure(null, e);
                }
            }

            @Override
            public void onFailure(Call<TT> call, Throwable t) {
                HandlerMessage tMessage = new HandlerMessage();
                tMessage.what = ERROR;
//                tMessage.obj = error;
                tMessage.whatTag = what;
                mListener.handlerMessage(tMessage);
            }
        });
    }

    /**
     * 获取 Json 型网络数据
     *
     * @param url  请求地址
     * @param clas 请求实体类
     * @param what 当前请求操作标识
     */
    public void getHttpPostJson(String url, Map<String, String> params,
                                final Class<?> clas, final int what) {
        HttpParams httpParams = new HttpParams();

        for (String key : params.keySet()) {
            httpParams.put(key, params.get(key));
        }

        HttpTools.post(url, null, httpParams).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                HandlerMessage tMessage = new HandlerMessage();
                tMessage.what = what;
                tMessage.obj = JsonUtils.toBean(clas, (String)response.body());

                mListener.handlerMessage(tMessage);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                HandlerMessage tMessage = new HandlerMessage();
                tMessage.what = ERROR;
//                tMessage.obj = error;
                mListener.handlerMessage(tMessage);
            }
        });
    }

    public void getHttpPostJson(String url, Map<String, String> params,
                                final int what) {
        HttpParams httpParams = new HttpParams();

        for (String key : params.keySet()) {
            httpParams.put(key, params.get(key));
        }

        HttpTools.post(url, null, httpParams).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                HandlerMessage tMessage = new HandlerMessage();
                tMessage.what = what;
                tMessage.obj = response.body();
                mListener.handlerMessage(tMessage);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                HandlerMessage tMessage = new HandlerMessage();
                tMessage.what = ERROR;
                // tMessage.obj = error;
                mListener.handlerMessage(tMessage);
            }
        });
    }

    public void postHeaderJson(String url, Map<String, String> headers, Map<String, String> params,
                               final int what) {

        HttpParams httpParams = new HttpParams();

        for (String key : headers.keySet()) {
            httpParams.putHeaders(key, headers.get(key));
        }

        for (String key : params.keySet()) {
            httpParams.put(key, params.get(key));
        }

        HttpTools.post(url, null, httpParams)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        HandlerMessage tMessage = new HandlerMessage();
                        tMessage.what = what;
                        tMessage.obj = response.body();
                        mListener.handlerMessage(tMessage);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        HandlerMessage tMessage = new HandlerMessage();
                        tMessage.what = ERROR;
                        //                tMessage.obj = error;
                        mListener.handlerMessage(tMessage);
                    }
                });

    }

    /**
     * 将map里的参数转换成key1=value1&key2=value2形式
     *
     * @param url
     * @param params
     * @return 返回路径+参数字符串
     */
    public String appendParameter(String url, Map<String, String> params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return url + "?" + builder.build().getQuery();
    }

    /**
     * 将map里的参数转换成key1=value1&key2=value2形式
     *
     * @param url
     * @param params
     * @return 只返回参数字符串
     */
    public static final String appendParams(String url,
                                            Map<String, String> params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getQuery();
    }

}
