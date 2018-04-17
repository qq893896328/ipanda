package cn.cntv.app.ipanda.di.components;

import android.app.Activity;

import cn.cntv.app.ipanda.di.PerActivity;
import cn.cntv.app.ipanda.di.modules.ActivityModule;
import dagger.Component;

/**
 * Created by yuerq on 16/6/30.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //Exposed to sub-graphs.
    Activity activity();
}

