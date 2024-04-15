package org.example.Sender;

import java.sql.Time;

// 报文段信息
public class SenderMessage {
    public int index;  // 标识/序号
    public Time sendTime; // 发送时刻
    public byte[] message; // 报文
    public boolean ifSended;   // 是否已发送
    public boolean ifRecieverConfirmed; // 是否已收到确认


}
