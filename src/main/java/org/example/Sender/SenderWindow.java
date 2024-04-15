package org.example.Sender;

import java.util.*;

// 发送窗口类
public class SenderWindow {
    int pStart;
    int pCur;
    int pTail;
    ArrayList<SenderMessage> senderWindowList;

    public SenderWindow(){
        pStart = 0;
        pCur=0;
        pTail=0;
        senderWindowList = new ArrayList<SenderMessage>();

    }

    public void addMessageToWindow(SenderMessage messageToAdd){
        senderWindowList.add(messageToAdd);
    }

}
