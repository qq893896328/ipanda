package cn.cntv.app.ipanda.data.source;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.domain.model.Favorite;
import cn.cntv.app.ipanda.domain.source.PandaRepository;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;
import rx.Observable;

/**
 * Created by yuerq on 16/7/4.
 */
@Singleton
public class PandaDataRepository implements PandaRepository {

    private DBInterface dbInterface;

    @Inject
    public PandaDataRepository() {
        dbInterface = DBInterface.getInstance();
    }

    @Override
    public Observable<PEArticle> getArticle(String articleId) {
        return PandaApi.getArticle(articleId);
    }

    @Override
    public boolean isFavorited(String favId) {
        return dbInterface.getFavorite(favId) != null;
    }

    @Override
    public void addOrRemoveFavorite(Favorite favorite, boolean add) {
        if (add) {
            dbInterface.insertOrUpdateFavorite(favorite.toEntity());
        } else {
            dbInterface.deleteFavorite(favorite.getId());
        }
    }
}
