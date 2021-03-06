package cn.cntv.app.ipanda.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.db.entity.LiveChinaChannelEntity;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig favoriteDaoConfig;
    private final DaoConfig playHistoryDaoConfig;
    private final DaoConfig liveChinaChannelDaoConfig;

    private final FavoriteDao favoriteDao;
    private final PlayHistoryDao playHistoryDao;
    private final LiveChinaChannelDao liveChinaChannelDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        favoriteDaoConfig = daoConfigMap.get(FavoriteDao.class).clone();
        favoriteDaoConfig.initIdentityScope(type);

        playHistoryDaoConfig = daoConfigMap.get(PlayHistoryDao.class).clone();
        playHistoryDaoConfig.initIdentityScope(type);

        liveChinaChannelDaoConfig = daoConfigMap.get(LiveChinaChannelDao.class).clone();
        liveChinaChannelDaoConfig.initIdentityScope(type);

        favoriteDao = new FavoriteDao(favoriteDaoConfig, this);
        playHistoryDao = new PlayHistoryDao(playHistoryDaoConfig, this);
        liveChinaChannelDao = new LiveChinaChannelDao(liveChinaChannelDaoConfig, this);

        registerDao(FavoriteEntity.class, favoriteDao);
        registerDao(PlayHistoryEntity.class, playHistoryDao);
        registerDao(LiveChinaChannelEntity.class, liveChinaChannelDao);
    }
    
    public void clear() {
        favoriteDaoConfig.getIdentityScope().clear();
        playHistoryDaoConfig.getIdentityScope().clear();
        liveChinaChannelDaoConfig.getIdentityScope().clear();
    }

    public FavoriteDao getFavoriteDao() {
        return favoriteDao;
    }

    public PlayHistoryDao getPlayHistoryDao() {
        return playHistoryDao;
    }

    public LiveChinaChannelDao getLiveChinaChannelDao() {
        return liveChinaChannelDao;
    }

}
