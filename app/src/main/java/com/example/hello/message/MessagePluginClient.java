package com.example.hello.message;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Stardust on 2017/5/10.
 */

public class MessagePluginClient {
    private static MessagePluginClient instance = new MessagePluginClient();
    MySocketActionAdapter adapter = new MySocketActionAdapter();
    IconnectListener listener;
    private static Context content;
    private onBeatCache beatCacheListener;

    public void setBeatCacheListener(onBeatCache beatCacheListener) {
        this.beatCacheListener = beatCacheListener;
    }

    public interface IconnectListener {
        void onState(int state);
    }

    public interface onBeatCache {
        void beatCache();
    }

    public void setListener(IconnectListener listener) {
        this.listener = listener;
    }

    public static MessagePluginClient getInstance(Context context) {
        content = context;
        return instance;
    }

    public static MessagePluginClient getInstance() {
        return instance;
    }


    private CopyOnWriteArrayList<MessageListener> mMessageListeners = new CopyOnWriteArrayList<>();

    public void addListener(MessageListener listener) {
        mMessageListeners.add(listener);
    }

    public boolean isConnected() {
        return mState == State.CONNECTED;
    }

    public void connectToServer(String s) {
        String[] split = s.split(":");
        if (split.length == 2) {
            Integer port = Integer.valueOf(split[1]);
            connectToServer(split[0], port);
        }
        listener.onState(mState);
    }

    public static class State {

        public static final int DISCONNECTED = 0;
        public static final int CONNECTING = 1;
        public static final int CONNECTED = 2;

        private final int mState;
        private final Throwable mException;

        public State(int state, Throwable exception) {
            mState = state;
            mException = exception;
        }

        public State(int state) {
            this(state, null);
        }

        public int getState() {
            return mState;
        }

        public Throwable getException() {
            return mException;
        }
    }

    private static final State STATE_CONNECTED = new State(State.CONNECTED);
    private static final State STATE_CONNECTING = new State(State.CONNECTING);

    private int mState;
    private IConnectionManager manager;


    public int getState() {
        return mState;
    }

    int reConnectCount;

    public void connectToServer(String host, int port) {
        reConnectCount = 1;
        if (mState != State.DISCONNECTED) {
            if (manager.isConnect()) manager.disconnect();
        }
        mState = State.CONNECTING;
        listener.onState(mState);
        ConnectionInfo info = new ConnectionInfo(host, port);
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setWriteByteOrder(ByteOrder.BIG_ENDIAN).setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength() {
                return 4;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                ByteBuffer bb = ByteBuffer.wrap(header);
                bb.order(byteOrder);
                return bb.getInt();
            }
        });
        manager = OkSocket.open(info);
        manager.option(okOptionsBuilder.build());
        manager.registerReceiver(adapter);
        //调用通道进行连接
        manager.connect();
    }

    class MySocketActionAdapter extends SocketActionAdapter {
        int count = 0;
        final HeartPacket heartPacket = new HeartPacket();

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
//          connect succeed
            mState = State.CONNECTED;
            listener.onState(mState);
//                sendDeviceName();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (manager.isConnect()) {
                        try {
                            count++;
                            Thread.sleep(4000);
                            manager.send(heartPacket);
                            if (count % (15) == 0) {
//                                间隔1分钟命中一下缓存
                                if (beatCacheListener != null) {
                                    beatCacheListener.beatCache();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(info, action, e);
//                Toast.makeText(MainActivity.this, "fail" + info.toString() + e.toString(), LENGTH_SHORT).show();
            Log.i("connect", "fail" + info.toString() + e.toString());
            mState = State.DISCONNECTED;
            listener.onState(mState);
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(info, action, e);
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            Log.i("receive data", ">>>");
            ByteBuffer byteBuffer = ByteBuffer.wrap(data.getHeadBytes());
            int length = byteBuffer.getInt();
            if (length > 0) {
                try {
                    final String s = new String(data.getBodyBytes(), Packet.CHARSET);
//                       接收到消息
                    handleData(s);
                    Log.i("data", s);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                if (manager == null) return;
                //是否是心跳返回包,需要解析服务器返回的数据才可知道
                manager.getPulseManager().feed();
            }
        }
    }


    private void handleData(String str) {
        Gson gson = new Gson();
        Message message = gson.fromJson(str, Message.class);
//        handleMessage(msg);
        for (MessageListener listener : mMessageListeners) {
            listener.onMessage(message);
        }
    }

    private void sendDeviceName() {
        String device = (Build.BRAND + Build.MODEL + Build.ID).replaceAll(" ", "");
        Message message = new Message("login", device, "");
        send(message);
    }

    public void send(Message message) {
        if (manager != null && message != null) {
            if ("".equals(message.getDeviceId())) {
                if (content != null) {
                    message.setDeviceId(Device.getAndroidId(content));
                }
            }
            Packet sendable = new Packet(message.getCmd(), message.getDeviceId(), message.getData());
            manager.send(sendable);
        }
    }


    public boolean close() {
        if (manager != null) {
            manager.disconnect();
            mState = State.DISCONNECTED;
            listener.onState(mState);
            return true;
        }
        return false;
    }
}
