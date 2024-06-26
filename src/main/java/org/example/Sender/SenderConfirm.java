package org.example.Sender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalTime;

// 确认接收线程类
public class SenderConfirm extends Thread {
    public SenderProcess senderProcess;
    public SenderWindow senderWindow;
    Socket senderConfirmSocket;
    static ObjectInputStream ackInputStream;

    public SenderConfirm(SenderProcess sp, SenderWindow sw) {
        senderProcess = sp;
        senderWindow = sw;
    }

    public void run() {
        try {
            senderConfirmSocket = new Socket("localhost", 8081);
            System.out.println("SenderConfirm已建立通信:" + senderConfirmSocket.getPort());
            ackInputStream = new ObjectInputStream(senderConfirmSocket.getInputStream());
            while (true) {
                try {
                    byte[] ackBytes = (byte[]) ackInputStream.readObject();
                    SenderACKMessage senderACKMessage = new SenderACKMessage(ackBytes);
                    senderProcess.lock.lock();
                    if (senderACKMessage.ackId == 65535) {
                        senderWindow.windowSize = senderACKMessage.newWindowSize;
                        System.out.println("SenderConfirm:更新窗口大小为" + senderWindow.windowSize);
                        senderProcess.logDisplay += "[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" + "SenderConfirm:更新窗口大小为" + senderWindow.windowSize + "\n";
                        senderWindow.pTail = senderWindow.pStart + senderWindow.windowSize - 1;
                    } else {
                        System.out.println("SenderConfirm:收到ACK，ACKID:" + senderACKMessage.ackId);
                        senderProcess.logDisplay += "[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" + "SenderConfirm:收到ACK，ACKID:" + senderACKMessage.ackId + "\n";
                        if (senderACKMessage.newWindowSize >= 0 && senderACKMessage.newWindowSize != senderWindow.windowSize) {
                            senderWindow.windowSize = senderACKMessage.newWindowSize;
                            System.out.println("SenderConfirm:更新窗口大小为" + senderWindow.windowSize);
                            senderProcess.logDisplay += "[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" + "SenderConfirm:更新窗口大小为" + senderWindow.windowSize + "\n";
                            senderWindow.pTail = senderWindow.pStart + senderWindow.windowSize - 1;
                        }
                        for (int i = 0; i < senderACKMessage.ackId; i++) {    //更新窗口中的报文段状态为已确认
                            if (senderWindow.senderWindowList.get(i) != null) {
                                senderWindow.senderWindowList.get(i).ifRecieverConfirmed = true;
                                senderWindow.senderWindowList.remove(i); // 移除已确认的报文段
                            }
                        }
                        senderWindow.ackRenewPtr(senderACKMessage.ackId, senderACKMessage.newWindowSize);//更新窗口指针
                    }
                    senderProcess.lock.unlock();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
