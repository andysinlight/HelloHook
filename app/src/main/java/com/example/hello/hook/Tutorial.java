package com.example.hello.hook;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Tutorial implements IXposedHookLoadPackage {

    private Field m;
    private Activity playActivity;

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.qennnsad.aknkaksd"))
            return;
        XposedBridge.log("load in com.qennnsad.aknkaksd!");
//        hookAllActivity(lpparam);
//        hookBalance(lpparam);
//        hookM(lpparam);
//        hookRequest(lpparam);

//        hookRoomLimit(lpparam);
//        hookMessage(lpparam);
        hookService(lpparam);
    }

    private void hookService(LoadPackageParam lpparam) throws ClassNotFoundException {
        log("hock service");
        Class<?> loadClass = lpparam.classLoader.loadClass("com.qennnsad.aknkaksd.data.websocket.WebSocketService");
        findAndHookMethod(loadClass, "a",String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("service");
                log(param.args[0]+"");
                super.beforeHookedMethod(param);
            }
        });
    }

    private void hookMessage(LoadPackageParam lpparam) {
        log("hookMessage");

        Class<?> aClass=null;
        try {
            aClass = Class.forName("com.qennnsad.aknkaksd.data.bean.websocket.RoomPublicMsg");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        findAndHookMethod("com.qennnsad.aknkaksd.presentation.ui.room.a.f", lpparam.classLoader,  "a", aClass, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("in hook setSay");
                log("args " + param.args.length);
                for (Object obj : param.args) {
//                        showField(obj,obj.getClass().getDeclaredFields());
                }
            }
        });
    }

    private void hookRoomLimit(LoadPackageParam lpparam) {
        log("hookRoomLimit");
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getPreview_time", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object thisObject = param.thisObject;
                Field[] fields = new Field[4];
                fields[0] = thisObject.getClass().getDeclaredField("preview_time");
                for (Field field : fields) {
                    if (field == null) continue;
                    field.setAccessible(true);
                    field.set(param.thisObject, 1000000);
                    Object o = field.get(thisObject);
                    XposedBridge.log("\t\t\t hookRoomLimit " + field.getName() + " = " + o);
                }
            }
        });
    }

    private void hookGson(final LoadPackageParam lpparam) {
        log("hookGson");
        findAndHookMethod(Gson.class, "fromJson", String.class, Class.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("before fromJson");
            }
        });

    }

    private void hookHandler(final LoadPackageParam lpparam) {
        findAndHookMethod(Handler.class, "post", Runnable.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("before post");
                try {
                    XposedBridge.log(new Throwable());
                } catch (Exception e) {

                }
//                hideTraces(param,lpparam ,"sendMessageDelayed");
            }
        });
    }

    private void hookM(final LoadPackageParam lpparam) {
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                playActivity = (Activity) param.thisObject;
                String name = playActivity.getClass().getName();
                XposedBridge.log("****************************" + name);
                if ("com.qennnsad.aknkaksd.presentation.ui.room.player.player.PlayerActivity".equals(name)) {
//                    hookHandler(lpparam);

                    XposedBridge.log("************PlayerActivity****************");
                    m = playActivity.getClass().getDeclaredField("m");
                    if (m != null) {
                        XposedBridge.log("get field m");
                        m.setAccessible(true);
                        m.set(playActivity, 10);
                        XposedBridge.log("set value " + 10);
                    }
                }
            }
        });
    }

    private void hookRequest(LoadPackageParam lpparam) {
        findAndHookMethod("com.qennnsad.aknkaksd.presentation.ui.room.player.player.PlayerActivity$16", lpparam.classLoader, "run", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object[] args = param.args;
                Object thisObject = param.thisObject;
                Field field = thisObject.getClass().getDeclaredField("a");
                if (field != null) {
                    XposedBridge.log("get field PlayActivity");
                    field.setAccessible(true);
                    Object obj = field.get(thisObject);
                    XposedBridge.log("activity is " + obj);
//                    Field m = obj.getClass().getDeclaredField("m");
                    if (m != null) {
                        XposedBridge.log("get field m");
                        m.setAccessible(true);
                        m.set(playActivity, 10);
                        XposedBridge.log("set value " + 10);
                    }
                }
                XposedBridge.log("***" + thisObject.toString());
                for (Object arg : args) {
                    XposedBridge.log(arg.toString());
                }
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


    private void hookAllActivity(final LoadPackageParam lpparam) {
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity thisObject = (Activity) param.thisObject;
                XposedBridge.log("****************************");
                XposedBridge.log("当前 Activity : " + thisObject.getClass().getName());
                if (thisObject.getClass().getName().contains("com.qennnsad.aknkaksd.presentation.ui.room.player.player.PlayerActivity")) {
                    hookMessage(lpparam);
                    return;
                }
//
//                Field[] fields = thisObject.getClass().getDeclaredFields();
//                Method[] methods = thisObject.getClass().getDeclaredMethods();
//
//                for (Method method : methods) {
//                    XposedBridge.log("方法名：" + method.getName());
//                    //获取本方法所有参数类型，存入数组
//                    Class<?>[] getTypeParameters = method.getParameterTypes();
//                    if (getTypeParameters.length == 0) {
//                        XposedBridge.log("此方法无参数");
//                    }
//                    for (Class<?> class1 : getTypeParameters) {
//                        String parameterName = class1.getName();
//                        XposedBridge.log("参数类型：" + parameterName);
//                        XposedBridge.log("参数class：" + class1.toString());
//                    }
//                }
//                XposedBridge.log("--------------------------");
//
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    try {
//                        field.set(thisObject, field.getName());
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                    Object o = field.get(thisObject);
//                    String value = o == null ? "null" : o.getClass().getName();
//                    XposedBridge.log("\t" + field.getName() + " = " + value);
//                }
                super.afterHookedMethod(param);
            }
        });
    }

    private void log(String str) {
        XposedBridge.log(str);
    }

    private void hideTraces(XC_MethodHook.MethodHookParam param, LoadPackageParam lpparam, String tag) throws NoSuchFieldException, IllegalAccessException {
        try {
            findAndHookMethod("java.lang.Thread", lpparam.classLoader, "getStackTrace", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    StackTraceElement[] result = (StackTraceElement[]) param.getResult();
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    //de.robv.android.xposed.XposedBridge
                    StackTraceElement[] result = (StackTraceElement[]) param.getResult();
                    log("当前类信息:------------------------------------------->" + new Gson().toJson(result));
                    if (result != null) {
                        StackTraceElement[] hooks = new StackTraceElement[result.length];
                        for (int i = 0; i < hooks.length; i++) {
                            StackTraceElement element = result[i];
                            log("当前类信息:" + new Gson().toJson(element));
                            String className = element.getClassName();
                            Class<? extends StackTraceElement> aClass = element.getClass();
                            if (className.contains("de.robv.android.xposed.XposedBridge")) {
                                log("类名命中:" + className);
                                className = className.replaceAll("de.robv.android.xposed.XposedBridge", "xxx.xxx.yy");
                                Field field = aClass.getDeclaredField("declaringClass");
                                field.setAccessible(true); // 暴力反射
                                field.set(element, className);
                                //methodName
                                //fileName
                                //                            aClass.getDeclaredMethod()
                                log("类名命中处理后:" + className);
                                log("类名命中处理后,对象获取:" + element.getClassName());
                            }
                            log("类名:" + className);
                            String fileName = element.getFileName();
                            String methodName = element.getMethodName();
                            if (fileName.contains("XposedBridge")) {
                                Field fieldII = aClass.getDeclaredField("fileName");
                                fieldII.setAccessible(true); // 暴力反射
                                fieldII.set(element, "sub");
                            }
                            if (methodName.contains("invokeOriginalMethodNative") || methodName.contains("handleHookedMethod")) {
                                Field fieldI = aClass.getDeclaredField("methodName");
                                fieldI.setAccessible(true); // 暴力反射
                                fieldI.set(element, "metheds");
                            }
                            hooks[i] = element;
                        }
                        param.setResult(hooks);
                        result = (StackTraceElement[]) param.getResult();
                        for (StackTraceElement element : result) {
                            String className = element.getClassName();
                            if (className.contains("xxx.xxx.yy")) {
                                log("包名过滤命中======>>>>>>>:" + className + "|===>>>" + element.toString());
                            }
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            log("#堆栈跟踪过滤#" + e);
        }
    }

    private void showField(Object thisObject, Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(thisObject, field.getName());
                Object o = field.get(thisObject);
                String value = o == null ? "null" : o.toString();
                XposedBridge.log("\t" + field.getName() + " = " + value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


}