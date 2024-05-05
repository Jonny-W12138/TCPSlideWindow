package org.example.Sender;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// 发送端进程类
public class SenderProcess extends Thread {
    public static String recieverIP;    // 接收端进程所在IP地址
    public static int recieverPort;     // 接收端进程所在端口号
    public static String textToSend;   // ui中要发送的字符串
    public static int textSendTimes = 1;   // ui中要发送的次数
    public static int counter;          // 报文标识累加器
    public static SenderWindow senderWindow;  // 发送窗口类
    public static SenderDataProcessor senderDataProcessor;
    public static SenderTimer senderTimer;
    public static SenderConfirm senderConfirm;  // 确认接收线程类
    public static ServerSocket senderSocket;
    public static Socket recieverSocket;
    public static String logDisplay;    // 日志显示
    static ObjectOutputStream objectOutputStream;  // Socket的输出流
    public Lock lock;

    public static Sender senderUI;

    static void createConnection() throws IOException {
        senderSocket = new ServerSocket(8080);
        recieverSocket = senderSocket.accept();
        System.out.println("Sender已建立通信！" + recieverSocket.getPort());
        logDisplay="[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" + "Sender已建立通信！" + recieverSocket.getPort()+"\n";

        // 获取接收端的IP和端口号
        recieverPort = recieverSocket.getPort();
        recieverIP = recieverSocket.getInetAddress().toString().substring(1);
        System.out.println("Reciever的IP是：" + recieverIP);
        logDisplay+="[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" + "Reciever的IP是：" + recieverIP+"\n";

        // 创建Socket输出流
        objectOutputStream = new ObjectOutputStream(recieverSocket.getOutputStream());

    }

    void createACKConnction() {

    }

    public static String getRecieverIP() {
        return recieverIP;
    }

    public static int getRecieverPort() {
        return recieverPort;
    }

    // senderAddMessage 发送端向窗口添加要发送的报文
    public static void senderAddMessage() throws IOException {
        for (int i = 0; i < textSendTimes; i++) {
            SenderMessage messageToSend = new SenderMessage();

            messageToSend.ifSended = false;
            messageToSend.ifRecieverConfirmed = false;
            messageToSend.index = counter;
            messageToSend.message = SenderDataProcessor.convertMessage(textToSend, counter);
            ++counter;

            System.out.println("Sender向窗口增加一条内容，index：" + messageToSend.index);
            senderWindow.addMessageToWindow(messageToSend);
            ++SenderWindow.elementNum;

            try {
                Thread.sleep(300); // 0.3秒的停顿
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // 仅供测试 运行时删除下列代码
        /*
        messageToSend.ifSended=true;
        messageToSend.ifRecieverConfirmed=false;
        messageToSend.sendTime = new Time(System.currentTimeMillis());
         */
        // 仅供测试 运行时删除上述代码
    }

    public void senderSendToReciever(byte[] msg) throws IOException {
        if(senderWindow.windowSize == 0) {   // 窗口大小为0，不需要发送
            return;
        }
        objectOutputStream.writeObject(msg);    // 向输出流中写入对象
        objectOutputStream.flush(); // 务必flush！
    }

    public SenderProcess() throws IOException {


    }

    // Start启动类调用
    public void run() {
        try {
            createConnection(); //sender创建数据包发送的Socket服务器端
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FlatLightLaf.install();

        try {
            UIManager.setLookAndFeel( new FlatIntelliJLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("Sender");
        frame.setContentPane(new Sender().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        counter = 0;

        lock = new Lock() {
            @Override
            public void lock() {

            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return false;
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {

            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };

        senderWindow = new SenderWindow(this);
        senderDataProcessor = new SenderDataProcessor();
        senderTimer = new SenderTimer(this, senderWindow);
        senderConfirm = new SenderConfirm(this, senderWindow);
        senderTimer.start();  // 开始运行Sender超时检测
        senderConfirm.start();  // 开始运行Sender确认接收线程
        System.out.println("Sender Run!");
        while (true) {
        }
    }
}
