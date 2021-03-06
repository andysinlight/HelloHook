package com.example.hello.net;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NetUtils {
    //    static String Base_Url = "http://47.97.213.144/api";
    static String Base_Url = "http://192.168.0.110";
    final static String TAG = "hello";

    public interface callResult {
        void result(boolean success, String result);
    }

    public static void sendPost(final String r_url, final String data, final callResult callResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(r_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("content-type", "application/json; charset=utf-8");
                    conn.setReadTimeout(20000);
                    conn.setConnectTimeout(20000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os;
                    os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            callResult.result(true, sb.toString());
                            break;
                        }
                        in.close();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    callResult.result(false, e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }


//    static String Base_Url = "http://192.168.0.108";
//    http://47.97.213.144/api/kwy_notify

    public static void Post(final String path, final Map<String, String> params, final callResult callResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(Base_Url + path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(20000);
                    conn.setConnectTimeout(20000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os;
                    os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(params));
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                        }
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            callResult.result(true, jsonObject.getString("data"));
                        } else {
                            callResult.result(false, jsonObject.getString("msg"));
                        }
                        in.close();
                    } else {
                        callResult.result(false, responseCode + "");
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    callResult.result(false, e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String getQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String key : params.keySet()) {
            String value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));
        }
        return result.toString();
    }


    private static boolean validate = false;

    public static void getState(final Activity activity, String id) {
        if (validate) return;
        HashMap<String, String> p = new HashMap<>();
        p.put("id", id);
        NetUtils.Post("/active_state", p, new NetUtils.callResult() {
            @Override
            public void result(boolean success, final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                    }
                });
                if (success) {
                    validate = true;
                    Log.d(TAG, result);
                } else {
                    validate = false;
                    Log.d(TAG, result);
                }
            }
        });
    }


}
