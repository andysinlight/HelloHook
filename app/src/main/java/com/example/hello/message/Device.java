package com.example.hello.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

//import ezy.assist.compat.SettingsCompat;

/**
 * Created by Stardust on 2017/12/2.
 */

public class Device {

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
