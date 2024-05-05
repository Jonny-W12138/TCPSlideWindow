package org.example.Receiver;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// 报文段接收端进程类
public class ReceiverProcess extends Thread {
    static ObjectInputStream objectInputStream;
    public static ServerSocket receiverConfirmSocket;
    public static Socket senderConfirmSocket;
    public static String textDisplay = "";
    public static int newWindowSize=5;
    ObjectOutputStream ackOutputStream;
    ReceiverReceive receiverReceive;
    ReceiverWindow receiverWindow;
    ReceiverConfirm receiverConfirm;
    public Lock lock;


    static int get_situation() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        return randNum;
    }

    static Socket receiverSocket;

    /*static void CreateConnection() throws IOException {
        receiverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + receiverSocket.getPort());
        textDisplay += "接收端已建立通信！" + receiverSocket.getPort() + "\n";

        objectInputStream = new ObjectInputStream(receiverSocket.getInputStream());
        try {
            while (true) {
                // 从流中读取对象并反序列化
                SenderMessage receivedMessage = (SenderMessage) objectInputStream.readObject();
                System.out.println("Receiver:收到Socket消息： " + receivedMessage.index);
                textDisplay += "Receiver:收到Socket消息： " + receivedMessage.index + "\n";
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public ReceiverProcess() throws IOException {
    }

    // Start启动类调用
    public void run() {
        receiverWindow = new ReceiverWindow(this);
        receiverReceive = new ReceiverReceive(this, receiverWindow);
        receiverConfirm = new ReceiverConfirm(receiverWindow, this);
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

        try {
            UIManager.setLookAndFeel( new FlatIntelliJLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver(receiverWindow).Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        /*
        try {
            CreateConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        System.out.println("Receiver Run!");
        receiverReceive.start();    // 运行receiverReceive线程 在其中尝试建立通信
        receiverConfirm.start();    // 运行receiverConfirm线程
        try {
            createConnection(); // 创建确认连接？应该在这里？？
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
        }
    }

    /*public static void main(String[] args) throws Exception {
        CreateConnection();
        while (true) ;
    }*/

    int getPStart() {
        if (receiverWindow == null)
            return -1;
        return receiverWindow.get_pStart();
    }

    int getPTail() {
        if (receiverWindow == null)
            return -1;
        return receiverWindow.get_pTail();
    }

    void createConnection() throws IOException {
        receiverConfirmSocket = new ServerSocket(8081);
        senderConfirmSocket = receiverConfirmSocket.accept();
        System.out.println("ReceiverConfirm已建立通信！" + senderConfirmSocket.getPort());
        // 创建Socket输出流
        ackOutputStream = new ObjectOutputStream(senderConfirmSocket.getOutputStream());
    }
}
