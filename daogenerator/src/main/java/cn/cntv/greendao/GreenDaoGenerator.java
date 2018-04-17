package cn.cntv.greendao;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by yuerq on 2016/6/6.
 */
public class GreenDaoGenerator {

    private static String entityPath = "cn.cntv.app.ipanda.db.entity";


    public static void main(String[] args) throws Exception {

        int dbVersion = 2;
        Schema schema = new Schema(dbVersion, "cn.cntv.app.ipanda.db.dao");

        schema.enableKeepSectionsByDefault();
        //addUserInfo(schema);
       // addCategory(schema);

        addFavorite(schema);
        addPlayHistory(schema);
        addLiveChinaChannel(schema);

        // todo 绝对路径,根据自己的路径设定， 例子如下
        String path = Config.DAO_PATH;
        new DaoGenerator().generateAll(schema, path);
    }

    private static void addFavorite(Schema schema) {

        Entity favorite = schema.addEntity("FavoriteEntity");
        favorite.setTableName("collect");
        favorite.setClassNameDao("FavoriteDao");
        favorite.setJavaPackage(entityPath);

//        favorite.addIdProperty().autoincrement();
        favorite.addStringProperty("object_id").primaryKey();
        favorite.addStringProperty("user_id");
        favorite.addStringProperty("collect_id");
        favorite.addStringProperty("object_title");
        favorite.addStringProperty("object_title2");
        favorite.addStringProperty("object_logo");
        favorite.addStringProperty("object_url");
        favorite.addStringProperty("video_pid");
        favorite.addLongProperty("collect_date");
        favorite.addIntProperty("collect_type");
        favorite.addIntProperty("pageSource").columnName("source");
        favorite.addStringProperty("product");
        favorite.addIntProperty("flag");
        favorite.addStringProperty("videoLength").columnName("videoLength");
        favorite.addStringProperty("isUploadSucc").columnName("isUploadSucc");
        favorite.addBooleanProperty("isShowEditUi").columnName("isShowEditUi");
        favorite.addBooleanProperty("isChecked").columnName("isChecked");
        favorite.addBooleanProperty("isSingleVideo").columnName("isSingleVideo");

        favorite.setHasKeepSections(true);

    }

    private static void addCategory(Schema schema) {

        Entity category = schema.addEntity("Category");
        category.setTableName("Category");
        category.setClassNameDao("CategoryDao");
        category.setJavaPackage(entityPath);

        category.addIdProperty().autoincrement();
        category.addStringProperty("name").unique().notNull();
        category.addStringProperty("url").notNull();
        category.addStringProperty("type");
        category.addStringProperty("order");

        category.setHasKeepSections(true);

    }

    private static void addUserInfo(Schema schema) {

        Entity userInfo = schema.addEntity("UserEntity");
        userInfo.setTableName("UserInfo");
        userInfo.setClassNameDao("UserDao");
        userInfo.setJavaPackage(entityPath);

        userInfo.addIdProperty().autoincrement();
        userInfo.addIntProperty("userId").unique().notNull().index();
        userInfo.addIntProperty("nickName");
        userInfo.addStringProperty("avatar");

        userInfo.setHasKeepSections(true);
    }

    private static void addPlayHistory(Schema schema) {

        Entity history = schema.addEntity("PlayHistoryEntity");
        history.setTableName("playhistory");
        history.setClassNameDao("PlayHistoryDao");
        history.setJavaPackage(entityPath);

//        history.addIdProperty().autoincrement();
        history.addIntProperty("sign");
        history.addStringProperty("videoType").columnName("videoTypeo");
        history.addStringProperty("title");
        history.addStringProperty("timeLenth").columnName("timeLenth");
        history.addStringProperty("vid");
        history.addStringProperty("videoImg").columnName("videoImg");
        history.addStringProperty("uid");
        history.addStringProperty("pid").primaryKey();
        history.addStringProperty("pageurl").columnName("pageurl");
        history.addStringProperty("ip");
        history.addLongProperty("playtime").columnName("playtime");
        history.addStringProperty("client");
        history.addStringProperty("clienttype").columnName("clienttype");
        history.addStringProperty("position");
        history.addBooleanProperty("ischeck");
        history.addBooleanProperty("isSingleVideo").columnName("isSingleVideo");
        history.setHasKeepSections(true);

    }



    private static void addLiveChinaChannel(Schema schema) {

        Entity live = schema.addEntity("LiveChinaChannelEntity");
        live.setTableName("livechina_chinnel");
        live.setClassNameDao("LiveChinaChannelDao");
        live.setJavaPackage(entityPath);

        //live.addIdProperty().autoincrement();
        live.addStringProperty("title").primaryKey();
        live.addStringProperty("url");
        live.addStringProperty("type");
        live.addStringProperty("order");

        live.setHasKeepSections(true);

    }


}
