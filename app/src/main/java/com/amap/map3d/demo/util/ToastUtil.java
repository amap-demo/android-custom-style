/**
 * 
 */
package com.amap.map3d.demo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class ToastUtil {

	private static Context context;
	private static Handler handler = new Handler(Looper.getMainLooper());

	public static void init(Context con) {
		context = con;
	}

	public static void show(final String info) {
		if (context != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						Toast.makeText(context, info, Toast.LENGTH_LONG).show();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
		}
		Log.e("amap",info);
	}

}
