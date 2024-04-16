package org.example.Sender;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Time;

// 发送端进程类
public class SenderProcess extends Thread{
    public static String recieverIP;    // 接收端进程所在IP地址
    public static int recieverPort;     // 接收端进程所在端口号
    public static String textToSend;   // ui中要发送的字符串
    public static int counter;          // 报文标识累加器
    public static SenderWindow senderWindow;  // 发送窗口类
    public static SenderDataProcessor senderDataProcessor;
    public static SenderTimer senderTimer;

    public static ServerSocket senderSocket;
    public static Socket recieverSocket;
    static ObjectOutputStream objectOutputStream;  // Socket的输出流

    public static Sender senderUI;

    static void createConnection() throws IOException {
        senderSocket = new ServerSocket(8080);
        recieverSocket = senderSocket.accept();
        System.out.println("Sender已建立通信！" + recieverSocket.getPort());

        // 获取接收端的IP和端口号
        recieverPort = recieverSocket.getPort();
        recieverIP = recieverSocket.getInetAddress().toString().substring(1);
        System.out.println("Reciever的IP是：" + recieverIP);

        // 创建Socket输出流
        objectOutputStream = new ObjectOutputStream(recieverSocket.getOutputStream());

    }

    void createACKConnction(){

    }

    public static String getRecieverIP() {
        return recieverIP;
    }

    public static int getRecieverPort() {
        return recieverPort;
    }

    // senderAddMessage 发送端向窗口添加要发送的报文
    public static void senderAddMessage() throws IOException {
        SenderMessage messageToSend = new SenderMessage();

        messageToSend.ifSended=false;
        messageToSend.ifRecieverConfirmed=false;
        messageToSend.index=counter;
        messageToSend.message= SenderDataProcessor.convertMessage(textToSend,counter);
        ++counter;

        System.out.println("Sender向窗口增加一条内容，index："+messageToSend.index);

        // 仅供测试 运行时删除下列代码
        /*
        messageToSend.ifSended=true;
        messageToSend.ifRecieverConfirmed=false;
        messageToSend.sendTime = new Time(System.currentTimeMillis());
         */
        // 仅供测试 运行时删除上述代码
        senderWindow.addMessageToWindow(messageToSend);
    }

    public void senderSendToReciever(SenderMessage msg) throws IOException {
        objectOutputStream.writeObject(msg);
        objectOutputStream.flush(); // 务必flush！
    }

    public SenderProcess() throws IOException {


    }

    // Start启动类调用
    public void run(){
        try {
            createConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("Sender");
        frame.setContentPane(new Sender().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        counter = 0;

        senderWindow = new SenderWindow(this);
        senderDataProcessor = new SenderDataProcessor();
        senderTimer = new SenderTimer(this, senderWindow);
        senderTimer.start();  // 开始运行Sender超时检测

        System.out.println("Sender Run!");
        while (true){}
    }
}
