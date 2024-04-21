package org.example.Reciever;

public class RecieverACKMessage {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    int newWindowSize;  // 新的窗口大小
}
