package org.example.Sender;

import java.io.Serializable;

public class SenderOriginMessage implements Serializable {
    public char type;   // 报文类型
    public int messageId;   // 报文标识
    public int dataLength;  // 数据长度
    public String data; // 字符串数据

    public SenderOriginMessage(int messageId, String data) {
        this.type = 0;
        this.messageId = messageId;
        this.dataLength = data.length();
        this.data = data;
    }

    public byte[] toBytes(){
        // 计算数据长度

        // 创建字节数组，总长度为1（报文类型）+ 2（报文标识）+ 2（数据长度）+ 数据长度
        byte[] messageBytes = new byte[1 + 2 + 2 + dataLength];

        // 设置报文类型为0
        messageBytes[0] = 0;

        // 设置报文标识，占2个字节，高位在前低位在后
        messageBytes[1] = (byte) ((messageId >> 8) & 0xFF); // 高位
        messageBytes[2] = (byte) (messageId & 0xFF); // 低位

        // 设置数据长度，占2个字节，高位在前低位在后
        messageBytes[3] = (byte) ((dataLength >> 8) & 0xFF); // 高位
        messageBytes[4] = (byte) (dataLength & 0xFF); // 低位

        // 设置数据部分
        byte[] dataBytes = data.getBytes();
        System.arraycopy(dataBytes, 0, messageBytes, 5, dataLength);

        return messageBytes;
    }
}
