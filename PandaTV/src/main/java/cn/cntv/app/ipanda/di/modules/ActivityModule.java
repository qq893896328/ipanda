package cn.cntv.app.ipanda.di.modules;

import android.app.Activity;

import cn.cntv.app.ipanda.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yuerq on 16/6/30.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    Activity activity() {
        return this.activity;
    }
}
