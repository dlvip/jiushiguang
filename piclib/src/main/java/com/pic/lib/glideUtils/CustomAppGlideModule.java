package com.pic.lib.glideUtils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by NING on 2018/7/16.
 */

@GlideModule
public final class CustomAppGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {

        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).setMemoryCacheScreens(2).build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

    }

}
