package com.example.hello.hook;

import android.app.Activity;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Tutorial implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.qennnsad.aknkaksd"))
            return;
        XposedBridge.log("load in com.qennnsad.aknkaksd!");
        hookAllActivity();
    }


    private void hookAllActivity() {
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity thisObject = (Activity) param.thisObject;
                XposedBridge.log("当前 Activity : " + thisObject.getClass().getName());
                Field[] fields = thisObject.getClass().getDeclaredFields();
                Method[] methods = thisObject.getClass().getDeclaredMethods();

                for (Method method : methods) {
                    XposedBridge.log("方法名："+method.getName());
                    //获取本方法所有参数类型，存入数组
                    Class<?>[] getTypeParameters = method.getParameterTypes();
                    if(getTypeParameters.length==0){
                        XposedBridge.log("此方法无参数");
                    }
                    for (Class<?> class1 : getTypeParameters) {
                        String parameterName = class1.getName();
                        XposedBridge.log("参数类型："+parameterName);
                    }
                    XposedBridge.log("****************************");
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
                    XposedBridge.log("\t\t\t" + field.getName() + " = " +value);
                }
                super.afterHookedMethod(param);
            }
        });
    }
}