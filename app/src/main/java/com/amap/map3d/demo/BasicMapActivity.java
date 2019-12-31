package com.amap.map3d.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.map3d.demo.customstyle.DownloadStyle;
import com.amap.map3d.demo.customstyle.UnzipStyleItem;
import com.amap.map3d.demo.customstyle.UnzipStyleZip;


/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class BasicMapActivity extends Activity implements OnClickListener, DownloadStyle.DownloadListener {
    private TextureMapView mapView;
    private AMap aMap;


    private UnzipStyleZip unzipStyle;
    private DownloadStyle downloadStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basicmap_activity);

        mapView = (TextureMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }



    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_style) {
            // 开启扫码

            startDownloadCustomStyle("http://cn-hangzhou.oss-pub.aliyun-inc.com/amap-api/comm/upload/mystyle_sdk_1577794047_0100.zip");

        }
    }

    private void startDownloadCustomStyle(String url) {
        if (downloadStyle == null) {
            downloadStyle = new DownloadStyle();
            downloadStyle.setDownloadListener(this);
        }

        downloadStyle.setUrl(url);
        downloadStyle.startDownload();
    }

    @Override
    public void onDownload(byte[] data) {
        if (data != null) {
            if (unzipStyle == null) {
                unzipStyle = new UnzipStyleZip();
            }

            // 解压自定义样式
            UnzipStyleItem unzipStyleItem = unzipStyle.unzipStyleByData(data);

            // 设置自定义样式
            if (unzipStyleItem != null && aMap != null) {
                aMap.setCustomMapStyle(new CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleData(unzipStyleItem.getStyleData())
                        .setStyleExtraData(unzipStyleItem.getStyleExtraData())
                        .setStyleTextureData(unzipStyleItem.getStyleTextureData())
                );
            }
        }
    }

}
