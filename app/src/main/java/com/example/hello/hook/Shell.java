package com.example.hello.hook;

import com.example.hello.utils.io.FileUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import dalvik.system.PathClassLoader;

public class Shell {
    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        hookClassLoader(lpparam);
        hookExpandFieldArray(lpparam);
    }


    static void hookClassLoader(XC_LoadPackage.LoadPackageParam lpparam) {
        log("hookClassLoader");

        findAndHookConstructor("com.SecShell.SecShell.SecPathClassLoader", lpparam.classLoader, String.class, PathClassLoader.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object arg = param.args[1];
                log("SecPathClassLoader construct");
                super.beforeHookedMethod(param);
            }
        });
    }


    private static void hookExpandFieldArray(XC_LoadPackage.LoadPackageParam lpparam) {
//        private static void expandFieldArray(Object instance, String fieldName, Object[] extraElements, boolean before) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        log("hookExpandFieldArray");
        findAndHookMethod("com.SecShell.SecShell.DexInstall", lpparam.classLoader, "expandFieldArray", Object.class, String.class, Object[].class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                Object instance = param.args[0];
                String fieldName = (String) param.args[1];
                Object[] extraElements = (Object[]) param.args[2];
                boolean before = (boolean) param.args[3];
                log(instance.toString() + "  " + fieldName + "  " + extraElements.toString() + "  " + before);
                Field jlrField = findField(instance, fieldName);
                Object[] original = (Object[]) jlrField.get(instance);
                Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length);
                if (before) {
                    System.arraycopy(original, 0, combined, extraElements.length, original.length);
                    System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
                } else {
                    System.arraycopy(original, 0, combined, 0, original.length);
                    System.arraycopy(extraElements, 0, combined, original.length, extraElements.length);
                }
                jlrField.set(instance, combined);
                log(instance.toString());
                for (Object o : combined) {
                    log("12");
//                    File file = (File) o;
//                    file
                    log(o.getClass().getName());
//                    FileUtils.copyFile(file,getFile(file.getName()) );
                }
                FileUtils.copyFile(new File("/data/data/com.qennnsad.aknkaksd/.cache/classes.jar"), getFile("classes"));
                FileUtils.copyFile(new File("/data/data/com.qennnsad.aknkaksd/base.apk"), getFile("base"));
//                /data/user/0/io.va.exposed/virtual/data/app/com.qennnsad.aknkaksd/base.apk
                return null;
            }
        });
    }

    static File getFile(String name) {
        String path = "/data/data/com.qennnsad.aknkaksd/dexxxxxx";
        File file = new File(path, name + ".dex");
        return file;
    }


    private static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

}
