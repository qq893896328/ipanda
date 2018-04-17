package cn.cntv.app.ipanda.data.net.retrofit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.cntv.app.ipanda.data.net.retrofit.toolbox.HttpParamsEntry;
import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yuerq on 16/7/8.
 */
public class Network {

    public static final int DEFAULT_TIME_OUT = 15 * 1000;

    private static Network ourInstance = new Network();

    public static Network getInstance() {
        return ourInstance;
    }

    private OkHttpClient mOkHttpClient;

    RequestBuilder mRequestBuilder;

    private Network() {
    }

    public void init(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
        mRequestBuilder = new RequestBuilder(mOkHttpClient);
    }

    public RequestBuilder getRequestBuilder() {
        return mRequestBuilder;
    }

    public static class RequestBuilder {

        private OkHttpClient mClient;

        CallFactory callFactory;

        public RequestBuilder(OkHttpClient client) {
            mClient = client;
        }

        public Request toRequest(ApiConnection.Builder builder) {

            int timeoutMs = builder.getTimeoutMs();

            OkHttpClient client = mClient.newBuilder()
                    .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                    .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                    .build();

            okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();

            okHttpRequestBuilder.url(builder.getUrl());

            if (!builder.isShouldCache()) {
                okHttpRequestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
            }

            HttpParams params = builder.getParams();

            for (final HttpParamsEntry entry : params.getHeaders()) {
                okHttpRequestBuilder.addHeader(entry.k, entry.v);
            }

            setConnectionParametersForRequest(okHttpRequestBuilder, builder);

            okhttp3.Request okHttpRequest = okHttpRequestBuilder.build();

            callFactory = new CallFactory(client);

            return okHttpRequest;

        }

        public static class CallFactory {
            private OkHttpClient client;

            public CallFactory(OkHttpClient client) {
                this.client = client;
            }

            public okhttp3.Call newCall(Request request) {

                return client.newCall(request);
            }
        }


        private void setConnectionParametersForRequest(okhttp3.Request.Builder builder, ApiConnection.Builder b) {

            switch (b.getMethod()) {
                case ApiConnection.Method.GET:
                    builder.get();
                    break;
                case ApiConnection.Method.DELETE:
                    builder.delete();
                    break;
                case ApiConnection.Method.POST:
                    builder.post(createRequestBody(b));
                    break;
                case ApiConnection.Method.PUT:
                    builder.put(createRequestBody(b));
                    break;
                case ApiConnection.Method.HEAD:
                    builder.head();
                    break;
                case ApiConnection.Method.OPTIONS:
                    builder.method("OPTIONS", null);
                    break;
                case ApiConnection.Method.TRACE:
                    builder.method("TRACE", null);
                    break;
                case ApiConnection.Method.PATCH:
                    builder.patch(createRequestBody(b));
                    break;
                default:
                    throw new IllegalStateException("Unknown method type.");
            }
        }

        private RequestBody createRequestBody(ApiConnection.Builder b) {

            HttpParams params = b.getParams();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                params.writeTo(bos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] body = bos.toByteArray();
            if (body == null) return null;

            return RequestBody.create(MediaType.parse(getBodyContentType(params)), body);
        }


        public String getBodyContentType(HttpParams params) {
            if (params.getContentType() != null) {
                return params.getContentType();
            } else {
                return "application/x-www-form-urlencoded; charset=" + HttpParams.CHARSET;
            }
        }
    }
}
