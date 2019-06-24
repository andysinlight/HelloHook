package com.example.hello.message;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by andy on 2019/2/27.
 */

public class Packet implements ISendable {
    String str = "";
    public static final String CHARSET = "utf-8";


    public Packet(String cmd, String deviceId, String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", cmd);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("data", data);
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        byte[] body = null;
        try {
            body = str.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
        int allLen = 4 + body.length;
        //创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(ByteOrder.BIG_ENDIAN);

        //写入消息头----消息头的内容就是消息体的长度
        buffer.putInt(body.length);

        //写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer.array();
    }
}
