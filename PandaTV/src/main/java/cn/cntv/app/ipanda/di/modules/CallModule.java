package cn.cntv.app.ipanda.di.modules;

import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.UICallWrapper;
import cn.cntv.app.ipanda.domain.executor.PostExecutionThread;
import cn.cntv.app.ipanda.domain.executor.ThreadExecutor;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yuerq on 16/7/8.
 */
@Module
public class CallModule {

    private Call okHttpCall;

    public CallModule(Call call) {
        this.okHttpCall = call;
    }

    @Provides
     Call provideCall(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread) {
        return new UICallWrapper(okHttpCall, threadExecutor, postExecutionThread);
    }
}
