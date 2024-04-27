package org.example.Reciever;

import org.example.Sender.SenderMessage;

import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.net.*;

// 报文段接收端进程类
public class RecieverProcess extends Thread {
    static ObjectInputStream objectInputStream;
    public static ServerSocket recieverConfirmSocket;
    public static Socket senderConfirmSocket;
    static ObjectOutputStream ackOutputStream;
    RecieverRecieve recieverRecieve;
    RecieverWindow recieverWindow;

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
    public void run() {

        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver().Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        recieverWindow = new RecieverWindow(this);
        recieverRecieve = new RecieverRecieve(this, recieverWindow);
        /*
        try {
            CreateConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        System.out.println("Reciever Run!");
        recieverRecieve.start();    // 运行recieverRecieve线程 在其中尝试建立通信
        while (true) {
        }
    }

    public static void main(String[] args) throws Exception {
        CreateConnection();
        while (true) ;
    }




    static void createConnection() throws IOException {

        recieverConfirmSocket = new ServerSocket(8081);
        senderConfirmSocket = recieverConfirmSocket.accept();
        System.out.println("RecieverConfirm已建立通信！" + recieverSocket.getPort());

        // 创建Socket输出流
        ackOutputStream = new ObjectOutputStream(senderConfirmSocket.getOutputStream());


    }


}
