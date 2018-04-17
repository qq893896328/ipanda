package cn.cntv.app.ipanda.di.components;

import android.content.Context;

import javax.inject.Singleton;

import cn.cntv.app.ipanda.di.modules.ApplicationModule;
import cn.cntv.app.ipanda.domain.executor.PostExecutionThread;
import cn.cntv.app.ipanda.domain.executor.ThreadExecutor;
import cn.cntv.app.ipanda.domain.source.PandaRepository;
import cn.cntv.app.ipanda.manager.TransformerManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import dagger.Component;

/**
 * Created by yuerq on 16/6/29.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs.
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    PandaRepository pandaRepository();
    TransformerManager transformerManager();

}
