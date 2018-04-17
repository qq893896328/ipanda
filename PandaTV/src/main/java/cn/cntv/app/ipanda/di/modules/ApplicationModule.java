package cn.cntv.app.ipanda.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.UIThread;
import cn.cntv.app.ipanda.data.executor.JobExecutor;
import cn.cntv.app.ipanda.data.source.PandaDataRepository;
import cn.cntv.app.ipanda.domain.executor.PostExecutionThread;
import cn.cntv.app.ipanda.domain.executor.ThreadExecutor;
import cn.cntv.app.ipanda.domain.source.PandaRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yuerq on 16/6/29.
 */
@Module
public class ApplicationModule {

    private final AppContext appContext;

    public ApplicationModule(AppContext appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return appContext;
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    PandaRepository providePandaRepository(PandaDataRepository pandaDataRepository) {
        return pandaDataRepository;
    }

}
