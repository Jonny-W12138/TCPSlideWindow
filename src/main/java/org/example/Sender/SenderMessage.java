package org.example.Sender;

import java.io.Serializable;
import java.sql.Time;

// 报文段信息
// 实现Serializable接口以便通过Socket发送时进行序列化
public class SenderMessage implements Serializable {
    public int index;  // 标识/序号
    public Time sendTime; // 发送时刻
    public byte[] message; // 报文
    public boolean ifSended;   // 是否已发送
    public boolean ifRecieverConfirmed; // 是否已收到确认

}
