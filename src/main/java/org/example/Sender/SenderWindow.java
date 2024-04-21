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
    ArrayList<SenderMessage>
            senderWindowList;
    Timer senderWindowTimer;

    public SenderWindow(SenderProcess sp) {
        pStart = 0;
        pCur = -1;
        pTail = 0;
        senderWindowList = new ArrayList<SenderMessage>();
        senderProcess = sp;
        senderWindowTimer = new Timer();

        // 模拟定时器，每隔一段时间检查超时重传
        senderWindowTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                while (pCur != -1 && pCur <= pTail) {
                    try {
                        sendMessageToReciever(pCur);
                        ++pCur;
                        System.out.println("Sender：主动发送下标" + pCur + "的报文！");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 100, 100); // 每隔1秒检查一次超时
    }

    public void addMessageToWindow(SenderMessage messageToAdd) {
        senderWindowList.add(messageToAdd);

        if (pCur == -1) {
            pStart = 0;
            pTail = 0;
            pCur = 0;
        } else {
            pTail = Math.min(pStart + windowSize - 1, senderWindowList.size() - 1);
        }

        // 供测试，添加一个数据立即发送出去
        /*try {
            this.sendMessageToReciever(senderWindowList.size() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    public void ackRenewPtr(int ackId, int newWindowSize) {
        if (ackId < pStart) {   // ACK已过期
            return;
        }
        for (int i = pStart; i < ackId; i++) {  // 更新已确认的报文为收到
            senderWindowList.get(i).ifRecieverConfirmed = true;
        }
        pStart = ackId;
        pCur = pStart;
        if (newWindowSize >= 0) {   // 更新窗口大小（如果有要求）
            windowSize = newWindowSize;
        }
        pTail = Math.min(pStart + windowSize - 1, senderWindowList.size() - 1); // 更新窗口尾指针
    }

    public void sendMessageToReciever(int index) throws IOException {
        SenderMessage message = senderWindowList.get(index);
        senderProcess.senderSendToReciever(message.message);    // 调用senderProcess的发送方法
        senderWindowList.get(index).ifSended = true;
        senderWindowList.get(index).sendTime = new Time(System.currentTimeMillis());
        System.out.println("Sender：senderProcess发送报文：" + index);
    }


}
