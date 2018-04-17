package cn.cntv.app.ipanda.presenter;

import cn.cntv.app.ipanda.ui.view.LoadDataView;

/**
 * Created by yuerq on 16/7/4.
 */
public interface Presenter<V extends LoadDataView> {

    void setView(V view);

    void resume();

    void pause();

    void destroy();

}
