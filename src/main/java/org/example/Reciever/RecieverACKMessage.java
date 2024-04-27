package org.example.Reciever;

import java.io.Serializable;

public class RecieverACKMessage implements Serializable {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    int newWindowSize;  // 新的窗口大小

    public RecieverACKMessage(int ackId, int newWindowSize) {
        this.type = 1;
        this.ackId = ackId;
        this.newWindowSize = newWindowSize;
    }

    public RecieverACKMessage(int ackId) {
        this.type = 1;
        this.ackId = ackId;
        this.newWindowSize = -1;    // -1表示不改变窗口大小
    }

    public byte[] toByte()
    {
        byte [] message=new byte[3];
        message[0]=toByte()[this.type];
        message[1]=toByte()[this.ackId];
        message[2]=toByte()[this.newWindowSize];
        return message;
    }


}
