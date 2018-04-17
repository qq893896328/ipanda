package cn.cntv.app.ipanda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.cntv.app.ipanda.db.dao.DaoMaster;
import cn.cntv.app.ipanda.db.dao.FavoriteDao;
import cn.cntv.app.ipanda.db.dao.LiveChinaChannelDao;
import cn.cntv.app.ipanda.db.dao.PlayHistoryDao;

/**
 * Created by yuerq on 16/6/24.
 */
public class PandaOpenHelper extends DaoMaster.OpenHelper {

    public PandaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {

            FavoriteDao.createTable(db, true);
            PlayHistoryDao.createTable(db, true);
            LiveChinaChannelDao.createTable(db, true);
        }
    }
}
