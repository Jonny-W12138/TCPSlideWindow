package org.example.Reciever;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;

// 接收窗口类
public class RecieverWindow {


    int size = 3;//窗口大小
    int message_sum = 0;//报文累加器
    static RecieverProcess recieverProcess;

    static HashMap<Integer, RecieverOriginMessage> MessageInfo_list;

    public static int pStart;//起始指针
    public static int pTail;//尾部指针

    public boolean is_empty() {
        if (pStart == pTail && pTail == MessageInfo_list.size())
            return true;
        return false;
    }

    public int get_pStart() {
        return pStart;
    }

    public int get_pTail() {
        return pTail;
    }

    public HashMap<Integer, RecieverOriginMessage> get_MessageInfo_list() {
        return MessageInfo_list;
    }


    public RecieverWindow(RecieverProcess rp) {
        pStart = 0;
        pTail = pStart + size;
        MessageInfo_list = new HashMap<>();
        recieverProcess = rp;
    }


    public static void Move(int IDmax)  //改变接收窗口的位置
    {
        pStart = IDmax;
        pTail = pStart + 2;
    }

    public int Get_IDmax()//获得待发送的确认的标识
    {
        int res = pStart;
        for (int i = pStart; i <= pTail; i++) {
            if (MessageInfo_list.get(i)!=null && (MessageInfo_list.get(i).messageId != -1) && !MessageInfo_list.get(i).is_confirm) {
                res = i;
            }
            else{
                break;
            }
        }
        System.out.println("【Debug】Reciever:MaxID：" + (res + 1));
        return res + 1;
    }

    public boolean Is_repeat(int ID)//判断是否是重复报文段
    {
        if (ID >= MessageInfo_list.size() || MessageInfo_list.get(ID) == null){
            return false;
        }
        return MessageInfo_list.get(ID).is_confirm;
    }


    public void Insert_Message(byte[] messageBytes, Time time)//插入报文段
    {
        int ID = RecieverDataProcessor.getMessageId(messageBytes);
        /*if(ID>=MessageInfo_list.size())
        {
            ArrayList<RecieverOriginMessage> list=new ArrayList<>(ID+5);
            for (int i=0;i<MessageInfo_list.size();i++) {
                list.set(i, MessageInfo_list.get(i));
            }
            MessageInfo_list = list;
        }*/
        MessageInfo_list.put(ID, new RecieverOriginMessage(-1, "", null));
        MessageInfo_list.get(ID).messageId = ID;
        MessageInfo_list.get(ID).dataLength = RecieverDataProcessor.getLength(messageBytes);
        MessageInfo_list.get(ID).is_confirm = false;
        MessageInfo_list.get(ID).data = RecieverDataProcessor.getData(messageBytes);
        MessageInfo_list.get(ID).Recieve_Time = time;
    }


    public static void Reciever_send_to_Sender(byte[] msg) throws IOException {
        recieverProcess.ackOutputStream.writeObject(msg);    // 向输出流中写入对象
        recieverProcess.ackOutputStream.flush(); // 务必flush！
    }


    public static void sendMessageToSender(RecieverACKMessage message) throws IOException {
        int index = message.ackId;
        byte[] ack = message.toByte();
        if(index == -1){
            System.out.println("Receiver：将sender窗口调整为：" + message.newWindowSize);
            RecieverProcess.textDisplay += "ReceiverConfirm：将sender窗口调整为：" + message.newWindowSize + "\n";
        }else{
            System.out.println("Receiver：向sender发送ACK报文：" + index);
            RecieverProcess.textDisplay += "ReceiverConfirm：向sender发送ACK报文：" + index + "\n";
        }
        for (int i = 0; i < index; i++) {
            if(MessageInfo_list.get(i)!=null){
                MessageInfo_list.get(i).is_confirm = true; // 将ackID之前的报文段标记为已确认
            }
        }
        Reciever_send_to_Sender(ack);
        Move(index);

    }

    public void show_error() {

    }

    public void show() {

    }


}


