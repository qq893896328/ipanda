package cn.cntv.app.ipanda.ui.view;

import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;

/**
 * Created by yuerq on 16/7/4.
 */
public interface PandaEyeDetailView extends LoadDataView {

    void showFavoriteIcon(boolean favorited);

    void favoriteSuccess();

    void favoriteFailed();

    void renderArticle(PEArticle article);
}
