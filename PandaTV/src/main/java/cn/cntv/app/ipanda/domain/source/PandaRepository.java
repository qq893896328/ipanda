package cn.cntv.app.ipanda.domain.source;

import cn.cntv.app.ipanda.domain.model.Favorite;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;
import rx.Observable;

/**
 * Created by yuerq on 16/7/4.
 */
public interface PandaRepository  {

    Observable<PEArticle> getArticle(String articleId);

    boolean isFavorited(String favId);

    void addOrRemoveFavorite(Favorite favorite, boolean add);
}
