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

    public void run(){
        try {
            try {
                Thread.sleep(1000); // 等待1秒钟， 等待接收端进程建立通信
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            senderConfirmSocket = new Socket("localhost", 8081);
            System.out.println("SenderConfirm已建立通信:" + senderConfirmSocket.getPort());

            while (true) {
                SenderACKMessage senderACKMessage = null;
                try {
                    senderACKMessage = (SenderACKMessage) ackInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("SenderConfirm:收到Socket消息：" + senderACKMessage.ackId);
                senderWindow.ackRenewPtr(senderACKMessage.ackId,senderACKMessage.newWindowSize);//更新窗口指针
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
