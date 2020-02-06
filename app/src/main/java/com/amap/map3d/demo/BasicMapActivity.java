package com.amap.map3d.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.map3d.demo.customstyle.DownloadStyle;
import com.amap.map3d.demo.customstyle.UnzipStyleItem;
import com.amap.map3d.demo.customstyle.UnzipStyleZip;
import com.amap.map3d.demo.qrcode.activity.CaptureActivity;
import com.amap.map3d.demo.qrcode.util.Constant;
import com.amap.map3d.demo.util.PremissionUtils;
import com.amap.map3d.demo.util.ToastUtil;


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
        setTitle("个性化地图Demo " + MapsInitializer.getVersion());

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
            startQrCode();
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
            if (unzipStyleItem != null && aMap != null && unzipStyleItem.getStyleData() != null) {
                aMap.setCustomMapStyle(new CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleData(unzipStyleItem.getStyleData())
                        .setStyleExtraData(unzipStyleItem.getStyleExtraData())
                        .setStyleTextureData(unzipStyleItem.getStyleTextureData())
                );
                ToastUtil.show("样式设置成功");
            } else {
                ToastUtil.show("样式解压失败，请确认二维码是否正确或过期");
            }
        } else {
            ToastUtil.show("样式下载失败, 请确认二维码是否正确或过期");
        }
    }

   /*************************************** 权限检查******************************************************/

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
    };

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private PremissionUtils permissionUtils = null;


    @Override
    protected void onResume() {
        try{
            super.onResume();
            mapView.onResume();
            if (Build.VERSION.SDK_INT >= 23) {

                if (permissionUtils == null) {
                    permissionUtils = new PremissionUtils(this);
                }
                if (isNeedCheck) {
                    permissionUtils.checkPermissions(needPermissions);
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }

    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        try{
            if (Build.VERSION.SDK_INT >= 23) {

                if (permissionUtils != null) {
                    isNeedCheck = permissionUtils.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
                }

            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /*************************************** 权限检查******************************************************/


    // 开始扫码
    private void startQrCode() {
        // 二维码扫码
//        Intent intent = new Intent(BasicMapActivity.this, CaptureActivity.class);
//        startActivityForResult(intent, Constant.REQ_QR_CODE);

        startDownloadCustomStyle("http://lbsnew.amap.com/dev/mapstyle/q-id?id=855481a2a8d3ea29eca3cc247845b664&t=1580902952240");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);

            Log.i("amap","scanResult " + scanResult);

            if (scanResult != null) {
                startDownloadCustomStyle(scanResult.trim());
            } else {
                ToastUtil.show("扫码失败,扫描内容为空");
            }
        } else {
            ToastUtil.show("扫码失败");
        }
    }

}
