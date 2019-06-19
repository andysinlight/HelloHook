package com.example.hello.hook;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Tutorial implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.qennnsad.aknkaksd"))
            return;
        XposedBridge.log("load in com.qennnsad.aknkaksd!");
//        hookAllActivity();
//        hookBalance(lpparam);
        hookRequest(lpparam);
//        hooMoney(lpparam);
    }

    private void hookRequest(LoadPackageParam lpparam) {
        XposedBridge.log("**************  WebSocketService");
        findAndHookMethod(Dialog.class, "show", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
    }


    private void hooMoney(LoadPackageParam lpparam) {
        XposedBridge.log("**************  hooMoney");
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getMoney", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getMoney " + param.toString());
                Object thisObject = param.thisObject;
                Field[] fields = new Field[4];
                fields[0] = thisObject.getClass().getDeclaredField("money");
                for (Field field : fields) {
                    if (field == null) continue;
                    field.setAccessible(true);
                    field.set(param.thisObject, 100);
                    Object o = field.get(thisObject);
                    XposedBridge.log("\t\t\t" + field.getName() + " = " + o);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });

        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getMoney", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getMoney " + param.toString());
                Object thisObject = param.thisObject;
                Field[] fields = new Field[4];
                fields[0] = thisObject.getClass().getDeclaredField("money");
                for (Field field : fields) {
                    if (field == null) continue;
                    field.setAccessible(true);
                    field.set(param.thisObject, 100);
                    Object o = field.get(thisObject);
                    XposedBridge.log("\t\t\t" + field.getName() + " = " + o);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
    }

    private void hookBalance(LoadPackageParam lpparam) {
        XposedBridge.log("**************  hookBalance");
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.me.UserMoney", lpparam.classLoader, "getCoinbalance", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getCoinbalance " + param.toString());
                Object thisObject = param.thisObject;
                Field[] fields = new Field[4];
                fields[0] = thisObject.getClass().getDeclaredField("beanbalance");
                fields[1] = thisObject.getClass().getDeclaredField("coinbalance");
                fields[2] = thisObject.getClass().getDeclaredField("pointbalance");
                fields[3] = thisObject.getClass().getDeclaredField("chipbalance");
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(param.thisObject, 100000);
                    Object o = field.get(thisObject);
                    XposedBridge.log("\t\t\t" + field.getName() + " = " + o);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });

        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.me.UserMoney", lpparam.classLoader, "getCoinbalance", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getCoinbalance " + param);
                XposedBridge.log(param.toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
    }


    private void hookAllActivity() {
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity thisObject = (Activity) param.thisObject;
                XposedBridge.log("****************************");
                XposedBridge.log("当前 Activity : " + thisObject.getClass().getName());
                Field[] fields = thisObject.getClass().getDeclaredFields();
                Method[] methods = thisObject.getClass().getDeclaredMethods();

                for (Method method : methods) {
                    XposedBridge.log("方法名：" + method.getName());
                    //获取本方法所有参数类型，存入数组
                    Class<?>[] getTypeParameters = method.getParameterTypes();
                    if (getTypeParameters.length == 0) {
                        XposedBridge.log("此方法无参数");
                    }
                    for (Class<?> class1 : getTypeParameters) {
                        String parameterName = class1.getName();
                        XposedBridge.log("参数类型：" + parameterName);
                    }
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        field.set(thisObject, field.getName());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    Object o = field.get(thisObject);
                    String value = o == null ? "null" : o.getClass().getName();
                    XposedBridge.log("\t\t\t" + field.getName() + " = " + value);
                }
                super.afterHookedMethod(param);
            }
        });
    }
}