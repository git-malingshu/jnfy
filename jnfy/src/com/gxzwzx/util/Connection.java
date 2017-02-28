package com.gxzwzx.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
	/** 
     * ≈–∂œÕ¯¬Á «∑Ò¡¨Õ® 
     * @param context 
     * @return 
     */ 
    public static boolean isNetworkConnected(Context context){  
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);  
        NetworkInfo info = cm.getActiveNetworkInfo();  
        return info != null && info.isConnected();    
    }

}
