package org.example.Sender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
                    System.out.println("SenderConfirm:收到ACK，ACKID:" + senderACKMessage.ackId);
                    senderProcess.logDisplay += "SenderConfirm:收到ACK，ACKID:" + senderACKMessage.ackId + "\n";
                    for (int i = 0; i < senderACKMessage.ackId; i++) {    //更新窗口中的报文段状态为已确认
                        senderWindow.senderWindowList.get(i).ifRecieverConfirmed = true;
                    }
                    senderWindow.ackRenewPtr(senderACKMessage.ackId, senderACKMessage.newWindowSize);//更新窗口指针
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
