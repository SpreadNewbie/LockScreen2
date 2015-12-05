package com.example.lockscreen;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {
	 /**
	     * 以最省内存的方式读取本地资源的图片
	     * @param context
	     * @param resId
	     * @return
	     */  
	    public static Bitmap readBitMap(Context context, int resId){  
	        BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	       opt.inPurgeable = true;  
	       opt.inInputShareable = true;  
	          //获取资源图片  
	       InputStream is = context.getResources().openRawResource(resId);  
	       Bitmap bitmap=BitmapFactory.decodeStream(is,null,opt);
	       try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	           return bitmap;  
	   }
}
