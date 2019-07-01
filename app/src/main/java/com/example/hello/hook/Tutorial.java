package com.example.hello.hook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.hello.message.Message;
import com.example.hello.message.MessageListener;
import com.example.hello.message.MessagePluginClient;
import com.example.hello.net.NetUtils;
import com.example.hello.utils.utils;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Tutorial implements IXposedHookLoadPackage {

    private Field m;
    private Activity playActivity;
    private String userId;
    private MessagePluginClient instance;
    private static Activity OtherUser;
    private Object meObject;
    int a;

    public int getA() {
        return 10000;
    }

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.qennnsad.aknkaksd"))
            return;
        XposedBridge.log("load in com.qennnsad.aknkaksd!");
        hookApplication(lpparam);
//        hookLoginInfo(lpparam);
//        hookAllActivity(lpparam);
        hookService(lpparam);
        hookLog(lpparam);
        hookPlay(lpparam);
        hookUpdate(lpparam);
        hookReConnect(lpparam);

        hookRoomLimit(lpparam);
//        hookMePresenter(lpparam);
        hookBaseResponse(lpparam);
//        hookConvertResponse(lpparam);
//        setImVisiale(lpparam);
//        hookFriendLimit(lpparam);

//        hookBalance(lpparam);
//        hookM(lpparam);
//        hookRequest(lpparam);

//        hookLoginInfo(lpparam);
//        hookUserInfo(lpparam);
//        hookMessage(lpparam);
    }

    private void hookReConnect(LoadPackageParam lpparam) {

    }

    private void hookUpdate(LoadPackageParam lpparam) {
        log("hookUpdate");
        findAndHookMethod("com.qennnsad.aknkaksd.util.q", lpparam.classLoader, "a", Context.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                log(param.toString());
                return "1.6.5";
            }
        });
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.websocket.WsLoginServerRequest", lpparam.classLoader, "setVer", String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                Object thisObject = param.thisObject;
                Field ver = thisObject.getClass().getDeclaredField("ver");
                ver.setAccessible(true);
                ver.set(thisObject, "1.6.5");
                return null;
            }
        });


    }

    private void hookPlay(LoadPackageParam lpparam) {
        Class<?> aClass = null;
        try {
            aClass = lpparam.classLoader.loadClass("com.qennnsad.aknkaksd.data.bean.user.UserBean");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (aClass == null) return;
//        public static WsLoginRequest a(String str) {
//        at com.qennnsad.aknkaksd.data.websocket.b.a(WsObjectPool.java:205)
        log("hookPlay");
        findAndHookMethod("com.qennnsad.aknkaksd.data.websocket.b", lpparam.classLoader, "a", String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                log(param.toString());
                log("replace hookPlay");
                return null;
            }
        });

//        log("hookReconnect");
//        findAndHookMethod("com.qennnsad.aknkaksd.data.websocket.WebSocketService", lpparam.classLoader, "g", new XC_MethodReplacement() {
//            @Override
//            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                log("replace g");
//                log(param.toString());
//                return null;
//            }
//        });


//        a(String str, String str2, long j)
        findAndHookMethod("com.qennnsad.aknkaksd.data.websocket.b", lpparam.classLoader, "a", String.class, String.class, long.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                log(param.toString());
                log("replace hookPlay");
                return null;
            }
        });
    }

    private void hookLog(LoadPackageParam lpparam) {
        log("hookLog");
        findAndHookMethod("com.qennnsad.aknkaksd.util.m", lpparam.classLoader, "a", boolean.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                boolean log = (boolean) param.args[0];
                log = true;
            }
        });
    }

    private void hookApplication(LoadPackageParam lpparam) {
        log("hookApplication");
        findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Application thisObject = (Application) param.thisObject;
                Object result = param.getResult();
//                showField(thisObject);
                String name = thisObject.getClass().getName();
                if (name.contains("BeautyLiveApplication")) {
                    utils.dump("aaaaaa.a", thisObject.getClass());
                }
                XposedBridge.log("****************************" + name);
            }
        });
    }

    private void hookConvertResponse(LoadPackageParam lpparam) {
        log("hookConvertResponse");
        Class<?> aClass = null;
        try {
            aClass = lpparam.classLoader.loadClass("okhttp3.ResponseBody");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (aClass == null) return;
        log("ResponseBody is load");
        findAndHookMethod("retrofit2.Converter", lpparam.classLoader, "convert", aClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("hookConvert method");
            }
        });
    }

    private void hookBaseResponse(LoadPackageParam lpparam) {
        log("hookBaseResponse");
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.BaseResponse", lpparam.classLoader, "getData", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                log("get a user info");
                Gson gson = new Gson();
                Object thisObject = param.thisObject;
                Field field = thisObject.getClass().getDeclaredField("data");
                field.setAccessible(true);
                Object data = field.get(thisObject);
                Log.d("hook_response", data.getClass().getName() + ":   " + gson.toJson(data));
            }
        });
    }

    private void hookMePresenter(LoadPackageParam lpparam) {
        log("hookMePresenter");
        findAndHookMethod("com.qennnsad.aknkaksd.presentation.ui.main.me.d", lpparam.classLoader, "a", Integer.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                meObject = param.thisObject;
                super.afterHookedMethod(param);
            }
        });
    }

    private void getUserInfo(String id) {
        log("get user info " + id);
        if (meObject == null || TextUtils.isEmpty(id)) return;
        try {
            Method a = meObject.getClass().getDeclaredMethod("a", Integer.class);
            a.setAccessible(true);
            a.invoke(meObject, Integer.valueOf(id));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void setImVisiale(LoadPackageParam lpparam) {
        log("setImVisiale");
        findAndHookConstructor("com.qennnsad.aknkaksd.data.bean.webview.WebUserInfo", lpparam.classLoader, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("setImVisiale" + param.args.toString());
                int arg = (int) param.args[1];
                arg = 1;
            }
        });
    }

    private void hookService(LoadPackageParam lpparam) throws ClassNotFoundException {
        log("hock service");
        Class<?> loadClass = lpparam.classLoader.loadClass("com.qennnsad.aknkaksd.data.websocket.WebSocketService");
        Class<?> wrClass = lpparam.classLoader.loadClass("com.qennnsad.aknkaksd.data.bean.websocket.WsRequest");
        findAndHookMethod(loadClass, "a", wrClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log(new Gson().toJson((Object) param.args[0]));
                super.beforeHookedMethod(param);
            }
        });

    }


    private void hookFriendLimit(LoadPackageParam lpparam) {
        log("hookFriendLimit");
        Class<?> aClass = null;
        try {
            aClass = lpparam.classLoader.loadClass("com.qennnsad.aknkaksd.data.bean.IsaddfriendBean");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (aClass == null) return;
//        a(IsaddfriendBean isaddfriendBean) {
        findAndHookMethod("com.qennnsad.aknkaksd.presentation.ui.main.me.OtherUserActivity", lpparam.classLoader, "a", aClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("isVerify");
            }
        });
    }

    private void hookRoomLimit(LoadPackageParam lpparam) {
        log("hookRoomLimit");

        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getPtid", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("getId");

                Object thisObject = param.thisObject;
                Field id = thisObject.getClass().getDeclaredField("ptid");
                id.setAccessible(true);
                id.set(thisObject, 0);
            }
        });

        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getBsid", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("getBsid");

                Object thisObject = param.thisObject;
                Field id = thisObject.getClass().getDeclaredField("bsid");
                id.setAccessible(true);
                id.set(thisObject, 0);
            }
        });
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean", lpparam.classLoader, "getPlid", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("getPlid");

                Object thisObject = param.thisObject;
                Field id = thisObject.getClass().getDeclaredField("plid");
                id.setAccessible(true);
                id.set(thisObject, 0);
            }
        });
    }

    private void hookLoginInfo(LoadPackageParam lpparam) {
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.user.LoginInfoAll", lpparam.classLoader, "getToken", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object thisObject = param.thisObject;
                Field[] fields = new Field[4];
                fields[0] = thisObject.getClass().getDeclaredField("user");
                for (Field field : fields) {
                    if (field == null) continue;
                    field.setAccessible(true);
                    Object userBean = field.get(param.thisObject);
                    Field id = userBean.getClass().getDeclaredField("id");
                    id.setAccessible(true);
                    userId = (String) id.get(userBean);
                    XposedBridge.log("LoginInfo user id is " + userId);
                }
            }
        });
    }

    private void hookUserInfo(LoadPackageParam lpparam) {
        XposedBridge.log("hookUserInfo ");
        findAndHookMethod("com.qennnsad.aknkaksd.data.bean.me.UserInfo", lpparam.classLoader, "getId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object thisObject = param.thisObject;
                showField(this);
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

    boolean validate = true;

    private void hookAllActivity(final LoadPackageParam lpparam) {
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final Activity thisObject = (Activity) param.thisObject;
                if (instance == null) {
                    instance = MessagePluginClient.getInstance(thisObject);
                    instance.setListener(new MessagePluginClient.IconnectListener() {
                        @Override
                        public void onState(int state) {
                            log("connect state is " + state);
                            if (state == 2) {
                                instance.sendDeviceName();
                            }

                        }
                    });
                    instance.setBeatCacheListener(new MessagePluginClient.onBeatCache() {
                        @Override
                        public void beatCache() {
                            log("beatCache" + instance.isConnected());
                        }
                    });
                    instance.connectToServer("47.97.213.144", 6789);
                    instance.addListener(new MessageListener() {
                        @Override
                        public void onMessage(Message message) {
                            log("on message " + message.toString());
                            String cmd = message.getCmd();
                            String data = message.getData();
                            if (cmd.equals("add_friend")) {
                                String[] split = data.split(",");
                                if (split.length == 2)
                                    addFriend(split[0], split[1]);
                            } else if (cmd.equals("user_info")) {
                                getUserInfo(data);
                            }
                        }
                    });
                }
                XposedBridge.log("****************************");
                String currentActivityName = thisObject.getClass().getName();
                XposedBridge.log("当前 Activity : " + currentActivityName);
                if (!TextUtils.isEmpty(userId)) {
                    log("/active_state start");
                    HashMap<String, String> p = new HashMap<>();
                    p.put("id", userId);
                    NetUtils.Post("/active_state", p, new NetUtils.callResult() {
                        @Override
                        public void result(boolean success, String result) {
                            if (success) {
                                validate = true;
                                log(result);
                            } else {
                                validate = false;
                                log(result);
                            }
                        }
                    });
                }

                if (currentActivityName.contains("com.qennnsad.aknkaksd.presentation.ui.room.player.player.PlayerActivity")) {
                    if (!validate) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisObject);
                        builder.setTitle("你好").setCancelable(false).
                                setMessage("破解程序试用中").setPositiveButton("马上激活", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> p = new HashMap<>();
                                p.put("id", userId);
                                NetUtils.Post("/active", p, new NetUtils.callResult() {
                                    @Override
                                    public void result(boolean success, final String result) {
                                        if (success) {
                                            log(result);
                                            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                            browserIntent.setData(Uri.parse(result));
                                            thisObject.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    thisObject.startActivity(browserIntent);
                                                }
                                            });
                                        } else {
                                            log(result);
                                        }
                                    }
                                });
                            }
                        });
                        builder.show();
                    }
                } else if (currentActivityName.contains("com.qennnsad.aknkaksd.presentation.ui.main.me.OtherUserActivity")) {
                    OtherUser = thisObject;
                    final Method addFriend = thisObject.getClass().getDeclaredMethod("u");
                    if (addFriend == null) {
                        XposedBridge.log("获取添加朋友方法失败");
                        return;
                    }
                    addFriend.setAccessible(true);
                    thisObject.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                XposedBridge.log("添加朋友 run");
                                addFriend.invoke(thisObject);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }


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

    private void showField(Object thisObject) {
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
                XposedBridge.log("参数class：" + class1.toString());
            }
        }
        XposedBridge.log("--------------------------");

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(thisObject, field.getName());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Object o = null;
            try {
                o = field.get(thisObject);
                String value = o == null ? "null" : o.getClass().getName();
                XposedBridge.log("\t" + field.getName() + " = " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void addFriend(String id, final String message) {
        log("add friend " + id + " " + message);
        log("OtherUser is " + OtherUser.toString());
        if (OtherUser == null) return;
        try {
            Field r = OtherUser.getClass().getDeclaredField("R");
            r.setAccessible(true);
            Object o = r.get(OtherUser);
            Field id_field = o.getClass().getDeclaredField("id");
            id_field.setAccessible(true);
            id_field.set(o, id);
//            r.set(OtherUser, id);
            final Method a = OtherUser.getClass().getDeclaredMethod("a", String.class, boolean.class);
            a.setAccessible(true);
            OtherUser.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        a.invoke(OtherUser, message, true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log("OtherUser is " + OtherUser.toString());
        }
    }


}