package org.example.Reciever;

import java.io.Serializable;

public class RecieverACKMessage implements Serializable {
    char type;  // 报文类型
    int ackId;  // 确认报文标识
    int newWindowSize;  // 新的窗口大小



}
