/**
 * 
 */
package com.amap.map3d.demo.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ToastUtil {

	private static Context context;

	public static void init(Context con) {
		context = con;
	}

	public static void show(String info) {
		if (context != null) {
			try {
				Toast.makeText(context, info, Toast.LENGTH_LONG).show();
			} catch (Throwable e) {
				e.printStackTrace();
				Log.e("amap",info);
			}
		}
	}

}
