package com.amap.map3d.demo.customstyle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author zxy
 * @data 2019-12-31
 */
public class UnzipStyleZip {


    /**
     * 不会把输入流进行关闭
     * @param inputStream
     * @return
     */
    private UnzipStyleItem unzipStyleByStream(InputStream inputStream) {
        try {
            UnzipStyleItem unzipStyleItem = new UnzipStyleItem();
            if (inputStream == null) {
                return null;
            }
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry ze;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    if (ze.getName().equals("style.data")) {
                        unzipStyleItem.setStyleData(readByteByStream(zipInputStream));
                    } else if (ze.getName().equals("style_extra.data")) {
                        unzipStyleItem.setStyleExtraData(readByteByStream(zipInputStream));
                    } else if (ze.getName().equals("textures.zip")) {
                        unzipStyleItem.setStyleTextureData(readByteByStream(zipInputStream));
                    }
                }

                zipInputStream.closeEntry();
            }
            zipInputStream.close();

            return unzipStyleItem;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] readByteByStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        outStream.close();
        return outStream.toByteArray();
    }



    /**
     * 从二进制读取
     * @return
     */
    public UnzipStyleItem unzipStyleByData(byte[] data) {
        if (data == null) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        UnzipStyleItem unzipStyleItem = unzipStyleByStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unzipStyleItem;
    }

    /**
     * 从sd卡读取
     * @param path
     * @return
     */
    public UnzipStyleItem unzipStyleBySDCardPath(String path) {

        try {
            File fHandler = new File(path);
            if (!fHandler.exists()) {
                // 判断当前文件是否存在
                return null;
            }

            FileInputStream inputStream = new FileInputStream(fHandler);
            UnzipStyleItem unzipStyleItem =  unzipStyleByStream(inputStream);
            inputStream.close();
            return unzipStyleItem;

        } catch (Throwable ex) {
            ex.printStackTrace();
        }


        return null;
    }


}
