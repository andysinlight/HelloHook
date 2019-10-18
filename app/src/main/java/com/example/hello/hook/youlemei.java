package com.example.hello.hook;

import com.example.hello.utils.io.FileUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.invokeOriginalMethod;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findField;

public class youlemei {
    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        log(lpparam.packageName + "hahaa");
//        hookClassLoader(lpparam);
//        hookResp(lpparam);
//        hookGson(lpparam);
        hookInterceptor(lpparam);
    }

    private static void hookGson(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("com.alibaba.fastjson.JSON", lpparam.classLoader, "parseObject",String.class,Class.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object resp = param.args[0];
                log(resp.toString());
//                invokeOriginalMethod("body", resp.toString(),null);

            }
        });
    }

    private static void hookInterceptor(XC_LoadPackage.LoadPackageParam lpparam) {
//        Class<?> aClass = findClass("okhttp3.Interceptor.C0123Chain", lpparam.classLoader);
        findAndHookMethod("com.yunbao.phonelive.AppContext", lpparam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("application onCreate after");
                Object thisObject = param.thisObject;
                Field sDeBug = thisObject.getClass().getField("sDeBug");
                sDeBug.setBoolean(thisObject,true);
            }
        });
    }

    private static void hookResp(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> aClass = findClass("com.lzy.okgo.model.Response", lpparam.classLoader);
        findAndHookMethod("com.yunbao.phonelive.http.HttpCallback", lpparam.classLoader, "onSuccess", aClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object resp = param.args[0];
                Method body = resp.getClass().getMethod("body");
                Object result = body.invoke(resp);
                log(result.toString());
//                invokeOriginalMethod("body", resp.toString(),null);

            }
        });
    }


    static void hookClassLoader(XC_LoadPackage.LoadPackageParam lpparam) {
        log("hookClassLoader");
//        findAndHookMethod("com.lzy.okgo.interceptor.HttpLoggingInterceptor", lpparam.classLoader, "log", String.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                Object arg = param.args[0];
//                log(arg.toString());
//            }
//        });

        try {
            Class<?> aClass = findClass("com.lzy.okgo.BuildConfig", lpparam.classLoader);
            Object o = aClass.newInstance();
            Field debug = aClass.getField("DEBUG");
            debug.set(o, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*


        public final class BuildConfig {
            public static final String APPLICATION_ID = "com.lzy.okgo";
            public static final String BUILD_TYPE = "release";
            public static final boolean DEBUG = false;
            public static final String FLAVOR = "";
            public static final int VERSION_CODE = 27;
            public static final String VERSION_NAME = "3.0.4";
        }
*/
    }


}
