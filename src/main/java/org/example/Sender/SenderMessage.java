package org.example.Sender;

import java.sql.Time;

// 报文段信息
public class SenderMessage {
    int index;  // 标识/序号
    Time sendTime; // 发送时刻
    byte[] message; // 报文
    boolean ifSended;   // 是否已发送
    boolean ifRecieverConfirmed; // 是否已收到确认
}
