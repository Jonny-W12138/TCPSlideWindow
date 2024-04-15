package org.example.Reciever;

import org.example.Sender.SenderMessage;

import java.util.Random;
import java.io.*;
import java.net.*;

// 接收端进程类
public class RecieverProcess extends Thread{
    static ObjectInputStream objectInputStream;
    static int get_situation() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        return randNum;
    }

    static Socket recieverSocket;

    static void CreateConnection() throws IOException {
        recieverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + recieverSocket.getPort());

        objectInputStream = new ObjectInputStream(recieverSocket.getInputStream());
        try {
            while (true) {
                // 从流中读取对象并反序列化
                SenderMessage receivedMessage = (SenderMessage) objectInputStream.readObject();
                System.out.println("Reciever:收到Socket消息： " + receivedMessage.index);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public RecieverProcess() throws IOException {
    }

    // Start启动类调用
    public void run(){
        try {
            CreateConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Reciever Run!");
        while (true){}
    }

    public static void main(String[] args) throws Exception {
        CreateConnection();
        while (true);
    }


}
