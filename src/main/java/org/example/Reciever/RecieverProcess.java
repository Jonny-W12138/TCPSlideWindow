package org.example.Reciever;

import org.example.Sender.SenderMessage;

import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.net.*;

// 接收端进程类
public class RecieverProcess extends Thread{
    static ObjectInputStream objectInputStream;
    public static ServerSocket recieverConfirmSocket;
    public static Socket senderConfirmSocket;
    static ObjectOutputStream ackOutputStream;

    static int get_situation() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        return randNum;
    }

    static Socket recieverSocket;



    public RecieverProcess() throws IOException {
    }

    // Start启动类调用
    public void run(){

        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver().Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

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

    static void createConnection() throws IOException {

        recieverConfirmSocket = new ServerSocket(8081);
        senderConfirmSocket = recieverConfirmSocket.accept();
        System.out.println("RecieverConfirm已建立通信！" + recieverSocket.getPort());

        // 创建Socket输出流
        ackOutputStream = new ObjectOutputStream(senderConfirmSocket.getOutputStream());

    }


}
