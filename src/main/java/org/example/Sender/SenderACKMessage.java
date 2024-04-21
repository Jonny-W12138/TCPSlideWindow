package org.example.Sender;

public class SenderACKMessage {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    int newWindowSize;  // 新的窗口大小

    public SenderACKMessage(int ackId) {
        this.type = 1;
        this.ackId = ackId;
    }
}