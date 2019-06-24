package com.example.hello.message;

/**
 * Created by andy on 2/28/2019.
 */
public class Message {
    private int code;
    private String message;
    private String cmd;
    private String deviceId;
    private String data;

    public Message() {
    }

    public Message(String cmd, String deviceId, String data) {
        this.cmd = cmd;
        this.deviceId = deviceId;
        this.data = data;
    }

    public Message(int code, String message, String cmd, String deviceId, String data) {
        this.code = code;
        this.message = message;
        this.cmd = cmd;
        this.deviceId = deviceId;
        this.data = data;
    }

    public Message(String cmd, String data) {
        this.cmd = cmd;
        this.deviceId = "";
        this.data = data;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
