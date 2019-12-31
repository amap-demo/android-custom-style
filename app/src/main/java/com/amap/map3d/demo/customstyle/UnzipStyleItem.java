package com.amap.map3d.demo.customstyle;

/**
 * @author zxy
 * @data 2019-12-31
 */
public class UnzipStyleItem {

    // 自定义样式二进制，使用二进制可以更快加载出自定义样式，如果设置了则不会读取styleDataPath
    private byte[] styleData = null;

    // 自定义样式纹理二进制，使用二进制可以更快加载出自定义样式，如果设置了则不会读取styleTexturePath
    private byte[] styleTextureData = null;

    // 样式额外的配置，比如路况，背景颜色等，使用二进制可以更快加载出自定义样式，如果设置了则不会读取styleExtraPath
    private byte[] styleExtraData = null;

    public byte[] getStyleData() {
        return styleData;
    }

    public void setStyleData(byte[] styleData) {
        this.styleData = styleData;
    }

    public byte[] getStyleTextureData() {
        return styleTextureData;
    }

    public void setStyleTextureData(byte[] styleTextureData) {
        this.styleTextureData = styleTextureData;
    }

    public byte[] getStyleExtraData() {
        return styleExtraData;
    }

    public void setStyleExtraData(byte[] styleExtraData) {
        this.styleExtraData = styleExtraData;
    }
}
