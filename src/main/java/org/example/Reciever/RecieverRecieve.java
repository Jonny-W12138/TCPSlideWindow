package org.example.Reciever;
import org.example.Sender.SenderACKMessage;
import org.example.Sender.SenderProcess;
import org.example.Sender.SenderWindow;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
// 报文段接收线程类
public class RecieverRecieve {
    private RecieverProcess rp;
    private RecieverWindow rw;
    Socket Reciever_recieve_Socket;


    public RecieverRecieve(RecieverProcess rp, RecieverWindow rw) {
        rp = rp;
        rw = rw;
    }


    public int get_error()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(4); // 生成一个0到3之间的随机数
        return randomNumber;
    }


    public void run(){
        try {
            try {
                Thread.sleep(1000); // 等待1秒钟， 等待接收端进程建立通信
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Reciever_recieve_Socket = new Socket("localhost", 8080);
            System.out.println("Reciever_recieve已建立通信:" + Reciever_recieve_Socket.getPort());

            while (true) {
                SenderACKMessage senderACKMessage = null;
                try {
                    senderACKMessage = (SenderACKMessage) ackInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Reviev：" + senderACKMessage.ackId);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
