package cn.cntv.app.ipanda.data.net.retrofit;

import java.io.IOException;

import cn.cntv.app.ipanda.domain.executor.PostExecutionThread;
import cn.cntv.app.ipanda.domain.executor.ThreadExecutor;
import okhttp3.Request;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Created by yuerq on 16/7/8.
 */
public class UICallWrapper<T> implements Call<T> {

    private Call<T> okHttpCall;

    ThreadExecutor threadExecutor;

    PostExecutionThread postExecutionThread;


    public UICallWrapper(Call okHttpCall, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.okHttpCall = okHttpCall;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public Response<T> execute() throws IOException {
        return okHttpCall.execute();
    }

    @Override
    public void enqueue(final Callback<T> callback) {

        Observable<Response<T>> observable = okHttpCall.getResult();

        observable
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(new Observer<Response<T>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(okHttpCall, e);
                    }

                    @Override
                    public void onNext(Response<T> response) {
                        callback.onResponse(okHttpCall, response);
                    }
                });

    }

    @Override
    public Observable<T> toObservable() {
        return okHttpCall.toObservable();
    }

    @Override
    public Observable<Response<T>> getResult() {
        return okHttpCall.getResult();
    }

    @Override
    public boolean isExecuted() {
        return okHttpCall.isExecuted();
    }

    @Override
    public void cancel() {
        okHttpCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return okHttpCall.isCanceled();
    }

    @Override
    public Call<T> clone() {
        return okHttpCall.clone();
    }

    @Override
    public Request request() {
        return okHttpCall.request();
    }
}
