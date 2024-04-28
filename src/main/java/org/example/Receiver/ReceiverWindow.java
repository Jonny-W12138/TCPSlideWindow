package org.example.Receiver;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;

// 接收窗口类
public class ReceiverWindow {


    int size = 3;//窗口大小
    int message_sum = 0;//报文累加器
    static ReceiverProcess receiverProcess;

    static HashMap<Integer, ReceiverOriginMessage> MessageInfo_list;

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

    public HashMap<Integer, ReceiverOriginMessage> get_MessageInfo_list() {
        return MessageInfo_list;
    }


    public ReceiverWindow(ReceiverProcess rp) {
        pStart = 0;
        pTail = pStart + size;
        MessageInfo_list = new HashMap<>();
        receiverProcess = rp;
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
        System.out.println("【Debug】Receiver:MaxID：" + (res + 1));
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
        int ID = ReceiverDataProcessor.getMessageId(messageBytes);
        /*if(ID>=MessageInfo_list.size())
        {
            ArrayList<ReceiverOriginMessage> list=new ArrayList<>(ID+5);
            for (int i=0;i<MessageInfo_list.size();i++) {
                list.set(i, MessageInfo_list.get(i));
            }
            MessageInfo_list = list;
        }*/
        MessageInfo_list.put(ID, new ReceiverOriginMessage(-1, "", null));
        MessageInfo_list.get(ID).messageId = ID;
        MessageInfo_list.get(ID).dataLength = ReceiverDataProcessor.getLength(messageBytes);
        MessageInfo_list.get(ID).is_confirm = false;
        MessageInfo_list.get(ID).data = ReceiverDataProcessor.getData(messageBytes);
        MessageInfo_list.get(ID).Receive_Time = time;
    }


    public static void Receiver_send_to_Sender(byte[] msg) throws IOException {
        receiverProcess.ackOutputStream.writeObject(msg);    // 向输出流中写入对象
        receiverProcess.ackOutputStream.flush(); // 务必flush！
    }


    public static void sendMessageToSender(ReceiverACKMessage message) throws IOException {
        int index = message.ackId;
        byte[] ack = message.toByte();
        if(index == -1){
            System.out.println("Receiver：将sender窗口调整为：" + message.newWindowSize);
            ReceiverProcess.textDisplay += "ReceiverConfirm：将sender窗口调整为：" + message.newWindowSize + "\n";
        }else{
            System.out.println("Receiver：向sender发送ACK报文：" + index);
            ReceiverProcess.textDisplay += "ReceiverConfirm：向sender发送ACK报文：" + index + "\n";
        }
        for (int i = 0; i < index; i++) {
            if(MessageInfo_list.get(i)!=null){
                MessageInfo_list.get(i).is_confirm = true; // 将ackID之前的报文段标记为已确认
            }
        }
        Receiver_send_to_Sender(ack);
        if(index != -1){
            Move(index);
        }

    }

    public void show_error() {

    }

    public void show() {

    }


}


