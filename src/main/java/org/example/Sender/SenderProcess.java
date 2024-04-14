package org.example.Sender;

import javax.swing.*;
import java.io.*;
import java.net.*;

// 发送端进程类
public class SenderProcess {
    public static String recieverIP;    // 接收端进程所在IP地址
    public static int recieverPort;     // 接收端进程所在端口号
    public static String textToSend;   // ui中要发送的字符串
    public int counter;          // 报文标识累加器
    public SenderWindow senderWindow;  // 发送窗口类

    public static ServerSocket senderSocket;
    public static Socket recieverSocket;

    public static Sender senderUI;

    static void createConnection() throws IOException {
        senderSocket = new ServerSocket(8080);
        recieverSocket = senderSocket.accept();
        System.out.println("Sender已建立通信！" + recieverSocket.getPort());

        recieverPort = recieverSocket.getPort();
        recieverIP = recieverSocket.getInetAddress().toString().substring(1);

        System.out.println("Reciever的IP是：" + recieverIP);
    }

    public static String getRecieverIP() {
        return recieverIP;
    }

    public static int getRecieverPort() {
        return recieverPort;
    }

    public static void main(String[] args) throws IOException {
        createConnection();
        JFrame frame = new JFrame("Sender");
        frame.setContentPane(new Sender().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        while (true){}
    }
}
