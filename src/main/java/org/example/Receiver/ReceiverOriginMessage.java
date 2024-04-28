package org.example.Receiver;

import java.io.Serializable;
import java.sql.Time;
public class ReceiverOriginMessage implements Serializable {
    public char type;   // 报文类型
    public int messageId;   // 报文标识
    public int dataLength=0;  // 数据长度
    public String data; // 字符串数据
    public boolean is_confirm;
    public Time Receive_Time;

    public ReceiverOriginMessage(int messageId, String data,Time time) {
        this.type = 0;
        this.messageId = messageId;
        this.dataLength = data.length();
        this.data = data;
        this.Receive_Time=time;
    }

}
