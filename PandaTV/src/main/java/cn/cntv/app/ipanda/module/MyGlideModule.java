package cn.cntv.app.ipanda.module;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;


public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int memoryCacheSize = (int) (0.4 * calculator.getMemoryCacheSize());
        int bitmapPoolSize = (int) (0.4 * calculator.getBitmapPoolSize());

        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSize));

        builder.setDiskCache(new DiskLruCacheFactory(AppConfig.DEFAULT_CACHE_PATH, 50 * 1024 * 1024));

//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}