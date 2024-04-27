package org.example.Reciever;

import org.example.Sender.SenderACKMessage;
import org.example.Sender.SenderProcess;
import org.example.Sender.SenderWindow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.Random;

// 报文段接收线程类
public class RecieverRecieve extends Thread {
    private static ObjectInputStream objectInputStream;
    private static RecieverProcess rp;
    private static RecieverWindow rw;
    static Socket recieverSocket;




    public RecieverRecieve(RecieverProcess rp, RecieverWindow rw) {
        rp = rp;
        rw = rw;
    }

    public void run() {
        try {
            CreateConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Reciever Run!");
    }


    public static int get_error() {
        Random random = new Random();
        int randomNumber = random.nextInt(4); // 生成一个0到3之间的随机数
        return randomNumber;
    }


    static void CreateConnection() throws IOException {
        recieverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + recieverSocket.getPort());

        objectInputStream = new ObjectInputStream(recieverSocket.getInputStream());
        try {
            while (true) {
                // 从流中读取对象并反序列化
                byte[] receivedMessage = (byte[]) objectInputStream.readObject();   // 从流中读取对象并反序列化
                int ID = RecieverDataProcessor.getMessageId(receivedMessage); // 获取报文ID
                int length = RecieverDataProcessor.getLength(receivedMessage); // 获取报文长度
                String data = RecieverDataProcessor.getData(receivedMessage); // 获取报文数据
                int error_type = get_error();
                if (error_type == 0) {
                    System.out.println("Reciever:收到Socket消息： " + ID);
                    Time receive_time=new Time(System.currentTimeMillis());
                    if (ID < rw.get_pStart() || ID > rw.get_pTail()) {
                        //show()
                    }
                    boolean result = rw.Is_repeat(ID);
                    if (result) {
                        int max_ID = rw.Get_IDmax();

                        //
                        rw.Move(max_ID);
                        rw.message_sum = 0;

                        RecieverACKMessage message=new RecieverACKMessage(max_ID);

                        rw.sendMessageToSender(message);
                        //show()
                    } else {
                        rw.Insert_Message(receivedMessage,receive_time);
                        rw.message_sum += 1;
                        if (rw.message_sum == 3) {

                            int max_ID = rw.Get_IDmax();

                            //
                            rw.Move(max_ID);
                            RecieverACKMessage message=new RecieverACKMessage(max_ID);
                            rw.sendMessageToSender(message);
                            rw.message_sum = 0;
                            //show()
                        } else {
                            //show()
                        }
                    }

                } else if (error_type == 1 || error_type == 2) {
                    System.out.println("error:" + error_type + " Message_ID:" + ID);
                    //show()展示
                } else {
                    //show()
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
