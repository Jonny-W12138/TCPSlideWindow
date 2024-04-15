package org.example.Sender;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

import static org.example.Sender.SenderProcess.senderWindow;

// 发送窗口类
public class SenderWindow {
    int pStart;
    int pCur;
    int pTail;
    int windowSize; // 窗口大小
    SenderProcess senderProcess;
    ArrayList<SenderMessage> senderWindowList;

    public SenderWindow(SenderProcess sp) {
        pStart = 0;
        pCur = 0;
        pTail = 0;
        senderWindowList = new ArrayList<SenderMessage>();
        senderProcess = sp;

    }

    public void addMessageToWindow(SenderMessage messageToAdd) {
        senderWindowList.add(messageToAdd);

        // 供测试，添加一个数据立即发送出去
        try {
            sendMessageToReciever(senderWindowList.size() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToReciever(int index) throws IOException {
        SenderMessage message = senderWindowList.get(index);
        senderProcess.senderSendToReciever(message);
        senderWindowList.get(index).ifSended = true;
        senderWindowList.get(index).sendTime = new Time(System.currentTimeMillis());
    }

}
