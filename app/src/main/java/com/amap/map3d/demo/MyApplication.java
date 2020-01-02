package com.amap.map3d.demo;

import android.app.Application;

import com.amap.map3d.demo.util.ToastUtil;

/**
 * @author zxy
 * @data 2020-01-02
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtil.init(this);

    }
}
