package cn.cntv.app.ipanda.manager;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.cntv.app.ipanda.domain.executor.PostExecutionThread;
import cn.cntv.app.ipanda.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by yuerq on 16/8/7.
 */
@Singleton
public class TransformerManager {

    private final Observable.Transformer schedulerTransformer;

    @Inject
    public TransformerManager(final ThreadExecutor threadExecutor, final PostExecutionThread postExecutionThread) {

        schedulerTransformer = new Observable.Transformer() {
            @Override
            public Object call(Object o) {
                return ((Observable) o).subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulerTransformer;
    }
}
