package cn.cntv.app.ipanda.di.components;

import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.di.PerActivity;
import cn.cntv.app.ipanda.di.modules.CallModule;
import dagger.Component;

/**
 * Created by yuerq on 16/7/8.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {CallModule.class})
public interface CallComponent {

    Call call();
}
