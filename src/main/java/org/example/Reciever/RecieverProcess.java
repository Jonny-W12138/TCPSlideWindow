package org.example.Reciever;

import java.util.Random;
import java.io.*;
import java.net.*;

// 接收端进程类
public class RecieverProcess extends Thread{
    static int get_situation() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        return randNum;
    }

    static Socket recieverSocket;

    static void CreateConnection() throws IOException {
        recieverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + recieverSocket.getPort());
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
