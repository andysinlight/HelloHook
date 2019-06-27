package com.example.hello.utils;

import java.io.File;

import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedBridge.log;

public class utils {

    public static void dump(String packageName, Class<?> aClass) {
        Object dexCache = XposedHelpers.getObjectField(aClass, "dexCache");
        log("decCache=" + dexCache);
//        Object o = XposedHelpers.callMethod(dexCache, "getDex");
//        byte[] bytes = (byte[]) XposedHelpers.callMethod(o, "getBytes");
//        String path = "/data/data/" + packageName + "/dump";
//        File file = new File(path, "source-" + bytes.length + ".dex");
//        if (file.exists()) {
//            log(file.getName() + " exists");
//            return;
//        }
//        FileUtils.writeByteToFile(bytes, file.getAbsolutePath());
    }
}
