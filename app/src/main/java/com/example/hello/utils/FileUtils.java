package com.example.hello.utils;

import java.io.FileOutputStream;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by wrbug on 2017/8/23.
 */
public class FileUtils {

    public static void writeByteToFile(byte[] data, String path) {
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(path);
            localFileOutputStream.write(data);
            localFileOutputStream.close();
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }
}
