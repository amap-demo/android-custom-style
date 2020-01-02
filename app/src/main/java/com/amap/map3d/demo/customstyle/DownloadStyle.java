package com.amap.map3d.demo.customstyle;

import android.util.Log;

import com.amap.map3d.demo.util.ThreadUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zxy
 * @data 2019-12-31
 */
public class DownloadStyle {

    String url = null;
    private DownloadListener downloadListener;

    public void setUrl(String url) {
        this.url = url;
    }

    public void startDownload() {

        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                startDownloadExec();
            }
        });

    }

    private void startDownloadExec() {

        try {
            URL u = new URL(url);
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((length = dis.read(buffer))>0) {
                bos.write(buffer, 0, length);
            }

            bos.close();
            is.close();

            if (downloadListener != null) {
                downloadListener.onDownload(bos.toByteArray());
            }

            return;

        } catch (MalformedURLException mue) {
            Log.e("amap", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("amap", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("amap", "security error", se);
        }
        if (downloadListener != null) {
            downloadListener.onDownload(null);
        }

    }

    public void setDownloadListener(DownloadListener listener) {
        this.downloadListener = listener;
    }



    public interface DownloadListener {
        void onDownload(byte[] dada);
    }

}
