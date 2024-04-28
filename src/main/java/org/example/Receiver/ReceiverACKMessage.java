package org.example.Receiver;

import java.io.Serializable;

public class ReceiverACKMessage implements Serializable {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    public int newWindowSize;  // 新的窗口大小

    public ReceiverACKMessage(int ackId, int newWindowSize) {
        this.type = 1;
        this.ackId = ackId;
        this.newWindowSize = newWindowSize;
    }

    public ReceiverACKMessage(int ackId) {
        this.type = 1;
        this.ackId = ackId;
        this.newWindowSize = ReceiverProcess.newWindowSize;
    }

    public byte[] toByte() {
        byte[] message = new byte[7];
        message[0] = (byte) this.type;
        message[1] = (byte) (this.ackId >> 8); // 高位字节
        message[2] = (byte) this.ackId;        // 低位字节
        message[3] = (byte) (this.newWindowSize >> 24); // 高位字节
        message[4] = (byte) (this.newWindowSize >> 16);        // 低位字节
        message[5] = (byte) (this.newWindowSize >> 8); // 高位字节
        message[6] = (byte) this.newWindowSize;        // 低位字节
        return message;
    }
}
