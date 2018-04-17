package cn.cntv.app.ipanda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import cn.cntv.app.ipanda.db.dao.DaoMaster;
import cn.cntv.app.ipanda.db.dao.DaoSession;
import cn.cntv.app.ipanda.db.dao.FavoriteDao;
import cn.cntv.app.ipanda.db.dao.LiveChinaChannelDao;
import cn.cntv.app.ipanda.db.dao.PlayHistoryDao;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.db.entity.LiveChinaChannelEntity;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import de.greenrobot.dao.query.DeleteQuery;

/**
 * Created by yuerq on 2016/6/6.
 */
public class DBInterface {

    private PandaOpenHelper openHelper;

    private DBInterface() {}

    private static class Factory {
       private static DBInterface INSTANCE = new DBInterface();
    }

    public static DBInterface getInstance() {
        return  Factory.INSTANCE;
    }

    public void initDbHelp(Context context) {

        openHelper = new PandaOpenHelper(context, "pandatvDB", null);
    }

    public void close() {
        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
        }
    }

    private DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private DaoSession openWritableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;

    }

    private void isInitOk() {

        if(null == openHelper) {
            throw new RuntimeException("DBInterface#init is not success or start, cause by openHelper is null");
        }
    }

//    public UserEntity getByUserId(int userId){
//        UserDao dao = openReadableDb().getUserDao();
//        UserEntity entity = dao.queryBuilder().where(UserDao.Properties.UserId.eq(userId)).unique();
//        return entity;
//    }
//
//    public void insertOrUpdateUser(UserEntity entity){
//        UserDao userDao =  openWritableDb().getUserDao();
//        long rowId = userDao.insertOrReplace(entity);
//    }

    /**---------------------收藏-----------------------------*/

    public void insertOrUpdateFavorite(FavoriteEntity favorite){
        FavoriteDao favoriteDao =  openWritableDb().getFavoriteDao();
        long rowId = favoriteDao.insertOrReplace(favorite);
    }


    public void deleteFavorite(String favId){
        FavoriteDao favoriteDao = openWritableDb().getFavoriteDao();
        DeleteQuery<FavoriteEntity> bd = favoriteDao.queryBuilder()
                .where(FavoriteDao.Properties.Object_id.eq(favId))
                .buildDelete();

        bd.executeDeleteWithoutDetachingEntities();
    }

    public FavoriteEntity getFavorite(String favId){

        FavoriteDao dao = openReadableDb().getFavoriteDao();
        FavoriteEntity entity = dao.queryBuilder().where(FavoriteDao.Properties.Object_id.eq(favId)).unique();
        return entity;
    }

    public List<FavoriteEntity> getFavoriteListByType(int type) {
        FavoriteDao dao = openReadableDb().getFavoriteDao();
        return dao.queryBuilder().where(
                FavoriteDao.Properties.Collect_type.eq(type))
                .orderDesc(FavoriteDao.Properties.Collect_date)
                .list();
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public List<FavoriteEntity> getFavoriteListByType(Object... type) {
        FavoriteDao dao = openReadableDb().getFavoriteDao();
        return dao.queryBuilder().where(FavoriteDao.Properties.Collect_type.in(type))
                .orderDesc(FavoriteDao.Properties.Collect_date)
                .list();
    }

    public void batchDeleteFavorate(List<FavoriteEntity> list) {
        FavoriteDao dao = openReadableDb().getFavoriteDao();
        dao.deleteInTx(list);
    }

    /**--------------下面开始播放历史 操作相关------------------------------*/

    public void insertOrUpdate(PlayHistoryEntity history) {
        PlayHistoryDao dao = openWritableDb().getPlayHistoryDao();
        dao.insertOrReplace(history);
    }

    public List<PlayHistoryEntity> getPlayHistoryByPid(String pid) {
        PlayHistoryDao dao = openReadableDb().getPlayHistoryDao();
        return dao.queryBuilder().where(
                PlayHistoryDao.Properties.Pid.eq(pid)).list();
    }

    public List<PlayHistoryEntity> getPlayHistoryOrderByPlayTime() {
        PlayHistoryDao dao = openReadableDb().getPlayHistoryDao();
        return dao.queryBuilder().orderDesc(PlayHistoryDao.Properties.Playtime)
                .list();
    }

    public void batchInsertOrUpdatePlayHistory(List<PlayHistoryEntity> list) {
        if (list.size() <= 0) {
            return;
        }
        PlayHistoryDao dao = openWritableDb().getPlayHistoryDao();
        dao.insertOrReplaceInTx(list);
    }

    public void deletePlayHistory(List<PlayHistoryEntity> list) {

        PlayHistoryDao dao = openWritableDb().getPlayHistoryDao();
        dao.deleteInTx(list);
    }


    /**----------------直播中国 ----------------------*/

    public List<LiveChinaChannelEntity> loadAllLiveChinaChannel() {
        LiveChinaChannelDao dao = openReadableDb().getLiveChinaChannelDao();
        return dao.queryBuilder().orderAsc(LiveChinaChannelDao.Properties.Order)
                .list();
    }

    public void insertOrUpdate(LiveChinaChannelEntity liveChina) {
        LiveChinaChannelDao dao = openWritableDb().getLiveChinaChannelDao();
        dao.insertOrReplace(liveChina);
    }

    public void deleteAllLiveChinaChannel() {
        LiveChinaChannelDao dao = openWritableDb().getLiveChinaChannelDao();
        dao.deleteAll();
    }

    public void batchInsertOrUpdateLiveChinaChannel(List<LiveChinaChannelEntity> list) {
        if (list.size() <= 0) {
            return;
        }
        LiveChinaChannelDao dao = openWritableDb().getLiveChinaChannelDao();
        dao.insertOrReplaceInTx(list);
    }

}
