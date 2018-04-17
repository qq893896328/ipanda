package cn.cntv.app.ipanda.di.components;

import cn.cntv.app.ipanda.di.PerActivity;
import cn.cntv.app.ipanda.di.modules.ActivityModule;
import cn.cntv.app.ipanda.ui.pandaeye.fragment.PandaEyeDetailFragment;
import dagger.Component;

/**
 * Created by yuerq on 16/7/4.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface FavoriteComponent extends ActivityComponent{
    void inject(PandaEyeDetailFragment fragment);
}
