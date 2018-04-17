package cn.cntv.app.ipanda.domain.model;

import java.io.Serializable;

import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;

/**
 * Created by yuerq on 16/7/5.
 */
public class Favorite implements Serializable {

    private String id;

    private String title;

    private String url;

    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FavoriteEntity toEntity() {
        FavoriteEntity entity = new FavoriteEntity();
        entity.setCollect_id(id);
        entity.setObject_id(id);
        entity.setObject_title(title);
        entity.setObject_logo(path);
        entity.setObject_url(url);
        entity.setCollect_date(System.currentTimeMillis());
        entity.setCollect_type(CollectTypeEnum.TW.value());

        return entity;
    }
}
