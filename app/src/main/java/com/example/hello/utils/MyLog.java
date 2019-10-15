package com.example.hello.utils;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;

public class MyLog {
   static String TAG="MyLog";
    public static void log(String sb){

        if (sb.length() > 4000) {
            Log.v(TAG, "sb.length = " + sb.length());
            int chunkCount = sb.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    String substring = sb.substring(4000 * i);
                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + substring);
                    XposedBridge.log(substring);
                } else {
                    String substring = sb.substring(4000 * i, max);
                    XposedBridge.log(substring);
                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + substring);
                }
            }
        } else {
            XposedBridge.log(sb);
        }
    }
}
