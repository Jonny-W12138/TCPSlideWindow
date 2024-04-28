package org.example.Reciever;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.net.*;

// 报文段接收端进程类
public class RecieverProcess extends Thread {
    static ObjectInputStream objectInputStream;
    public static ServerSocket recieverConfirmSocket;
    public static Socket senderConfirmSocket;
    public static String textDisplay = "";
    public static int newWindowSize=5;
    ObjectOutputStream ackOutputStream;
    RecieverRecieve recieverRecieve;
    RecieverWindow recieverWindow;
    RecieverConfirm recieverConfirm;


    static int get_situation() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        return randNum;
    }

    static Socket recieverSocket;

    /*static void CreateConnection() throws IOException {
        recieverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + recieverSocket.getPort());
        textDisplay += "接收端已建立通信！" + recieverSocket.getPort() + "\n";

        objectInputStream = new ObjectInputStream(recieverSocket.getInputStream());
        try {
            while (true) {
                // 从流中读取对象并反序列化
                SenderMessage receivedMessage = (SenderMessage) objectInputStream.readObject();
                System.out.println("Reciever:收到Socket消息： " + receivedMessage.index);
                textDisplay += "Reciever:收到Socket消息： " + receivedMessage.index + "\n";
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public RecieverProcess() throws IOException {
    }

    // Start启动类调用
    public void run() {
        recieverWindow = new RecieverWindow(this);
        recieverRecieve = new RecieverRecieve(this, recieverWindow);
        recieverConfirm = new RecieverConfirm(recieverWindow);

        try {
            UIManager.setLookAndFeel( new FlatIntelliJLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver(recieverWindow).Receiver);
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
        System.out.println("Reciever Run!");
        recieverRecieve.start();    // 运行recieverRecieve线程 在其中尝试建立通信
        recieverConfirm.start();    // 运行recieverConfirm线程
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
        if (recieverWindow == null)
            return -1;
        return recieverWindow.get_pStart();
    }

    int getPTail() {
        if (recieverWindow == null)
            return -1;
        return recieverWindow.get_pTail();
    }

    void createConnection() throws IOException {
        recieverConfirmSocket = new ServerSocket(8081);
        senderConfirmSocket = recieverConfirmSocket.accept();
        System.out.println("RecieverConfirm已建立通信！" + senderConfirmSocket.getPort());
        textDisplay += "RecieverConfirm已与Sender建立通信！" + senderConfirmSocket.getPort() + "\n";
        // 创建Socket输出流
        ackOutputStream = new ObjectOutputStream(senderConfirmSocket.getOutputStream());
    }
}
