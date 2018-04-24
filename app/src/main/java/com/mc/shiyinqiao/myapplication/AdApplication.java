package com.mc.shiyinqiao.myapplication;

import android.app.Application;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by shiyinqiao on 2018/4/23.
 */

public class AdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MC_LOG", "init imageLoader");
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }
}
