package org.example.Sender;

import java.io.Serializable;

public class SenderACKMessage implements Serializable {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    int newWindowSize;  // 新的窗口大小

    public SenderACKMessage(int ackId) {
        this.type = 1;
        this.ackId = ackId;
    }

    public SenderACKMessage(byte[] bytes) {
        this.type = (char) bytes[0];
        this.ackId = ((bytes[1] & 0xFF) << 8) | (bytes[2] & 0xFF);
        this.newWindowSize = ((bytes[3] & 0xFF) << 24) | ((bytes[4] & 0xFF) << 16) | ((bytes[5] & 0xFF) << 8) | (bytes[6] & 0xFF);
    }
}
