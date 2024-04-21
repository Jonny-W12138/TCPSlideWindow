package org.example.Reciever;

import java.io.Serializable;

public class RecieverOriginMessage implements Serializable {
    public char type;   // 报文类型
    public int messageId;   // 报文标识
    public int dataLength;  // 数据长度
    public String data; // 字符串数据
    public boolean is_confirm;

    public RecieverOriginMessage(int messageId, String data) {
        this.type = 0;
        this.messageId = messageId;
        this.dataLength = data.length();
        this.data = data;
    }

}
