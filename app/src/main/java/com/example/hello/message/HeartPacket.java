package com.example.hello.message;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by andy on 2019/2/27.
 */

public class HeartPacket implements IPulseSendable {

    @Override
    public byte[] parse() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(0);
        return byteBuffer.array();
    }
}
