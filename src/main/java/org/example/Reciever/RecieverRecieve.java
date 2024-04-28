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
    static Socket recieverSocket;// 对话框日志显示
    public static int portNum;

    public static byte[] verifyChecksum(byte[] dataWithChecksum) {
        byte[] data = new byte[dataWithChecksum.length - 2];
        System.arraycopy(dataWithChecksum, 0, data, 0, data.length);

        int checksumValue = 0;
        int carry = 0;

        // 对每16位进行计算
        for (int i = 0; i < data.length; i++) {
            checksumValue += (data[i] & 0xFF) << 8;
            if ((checksumValue & 0xFFFF0000) != 0) {
                checksumValue &= 0xFFFF;
                checksumValue++;
            }

            i++;
            if (i < data.length) {
                checksumValue += (data[i] & 0xFF);
                if ((checksumValue & 0xFFFF0000) != 0) {
                    checksumValue &= 0xFFFF;
                    checksumValue++;
                }
            }
        }

        // 取反得到校验和
        checksumValue = ~checksumValue & 0xFFFF;

        // 检查校验和是否匹配
        byte[] checksumBytes = new byte[2];
        checksumBytes[0] = (byte) (checksumValue >> 8);
        checksumBytes[1] = (byte) (checksumValue & 0xFF);

        if (checksumBytes[0] == dataWithChecksum[dataWithChecksum.length - 2] &&
                checksumBytes[1] == dataWithChecksum[dataWithChecksum.length - 1]) {
            // 校验和匹配，返回去除校验和后的数据
            return data;
        } else {
            // 校验和不匹配，返回空数组或者抛出异常，具体操作视情况而定
            return new byte[0];
        }
    }


    public RecieverRecieve(RecieverProcess rpinput, RecieverWindow rwinput) {
        rp = rpinput;
        rw = rwinput;
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
        int randomNumber = random.nextInt(5); // 生成一个0到4之间的随机数
        if (randomNumber < 3) { // 50%的概率生成0
            return 0;
        } else if (randomNumber < 4) { // 25%的概率生成1
            return 1;
        } else { // 25%的概率生成2
            return 2;
        }
    }


    static void CreateConnection() throws IOException {
        recieverSocket = new Socket("localhost", 8080);
        System.out.println("接收端已建立通信！" + recieverSocket.getPort());
        portNum = recieverSocket.getPort();

        objectInputStream = new ObjectInputStream(recieverSocket.getInputStream());
        try {
            while (true) {
                // 从流中读取对象并反序列化
                byte[] receivedMessage = (byte[]) objectInputStream.readObject();   // 从流中读取对象并反序列化
                receivedMessage=verifyChecksum(receivedMessage);
                int ID = RecieverDataProcessor.getMessageId(receivedMessage); // 获取报文ID
                int length = RecieverDataProcessor.getLength(receivedMessage); // 获取报文长度
                String data = RecieverDataProcessor.getData(receivedMessage); // 获取报文数据
                int error_type = get_error();

                // 方便调试 临时设置为0
                // error_type = 0;
                System.out.println("Reciever:收到Socket消息" + ID +":"+data);
                RecieverProcess.textDisplay += "RecieverRecieve:收到Socket消息" + ID +":"+data +"\n";
                if (error_type == 0) {
                    Time receive_time=new Time(System.currentTimeMillis());
                    if (ID < rw.get_pStart() || ID > rw.get_pTail()) {
                        //show()
                    }
                    boolean result = rw.Is_repeat(ID);
                    if (result) {
                        System.out.println("Reciecer消息ID:" + ID + "重复");
                        RecieverProcess.textDisplay += "Reciecer消息ID:" + ID + "重复" + "\n";
                        int max_ID = rw.Get_IDmax();

                        //
                        rw.Move(max_ID-1);
                        rw.message_sum = 0;

                        RecieverACKMessage message=new RecieverACKMessage(max_ID);

                        rw.sendMessageToSender(message);
                        //show()
                    } else {
                        rw.Insert_Message(receivedMessage,receive_time);
                        rw.message_sum += 1;
                        if (rw.message_sum == 3) {

                            //
                            // rw.Move(max_ID);
                            int max_ID = rw.Get_IDmax();
                            RecieverACKMessage message=new RecieverACKMessage(max_ID);
                            rw.sendMessageToSender(message);
                            rw.message_sum = 0;
                            max_ID = rw.Get_IDmax(); // 再次获取，用于更新窗口
                            rw.Move(max_ID-1);
                            //show()
                        } else {
                            //show()
                        }
                    }
                }
                else if (error_type == 1 || error_type == 2) {
                    System.out.println("消息ID:" + ID + "错误，类型为：" + error_type);
                    RecieverProcess.textDisplay += "消息ID:" + ID + "错误，类型为：" + error_type + "\n";
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
