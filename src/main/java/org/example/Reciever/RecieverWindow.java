package org.example.Reciever;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.example.Sender.SenderMessage;
import org.example.Sender.SenderProcess;
import sun.plugin2.message.Message;
import org.example.Sender.SenderDataProcessor;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import org.example.Reciever.RecieverDataProcessor;
import org.example.Reciever.RecieverRecieve;
// 接收窗口类
public class RecieverWindow{


    int size=3;//窗口大小
    int message_sum=0;//报文累加器
    static RecieverProcess recieverProcess;

    ArrayList<RecieverOriginMessage> MessageInfo_list;

    private int pStart;//起始指针
    private int pTail;//尾部指针

    public boolean is_empty()
    {
        if (pStart==pTail&&pTail==MessageInfo_list.size())
            return true;
        return false;
    }

    public int get_pStart()
    {
        return pStart;
    }
    public int get_pTail()
    {
        return pTail;
    }
    public ArrayList<RecieverOriginMessage> get_MessageInfo_list()
    {
        return MessageInfo_list;
    }


    public RecieverWindow(RecieverProcess rp) {
        pStart = 0;
        pTail = 0;
        MessageInfo_list=new ArrayList<>();
        recieverProcess = rp;
    }


    public void Move(int IDmax)  //改变接收窗口的位置
    {
        pStart = IDmax + 1;
        pTail = pStart + 4;
    }

    public int Get_IDmax()//获得待发送的确认的标识
    {
        for (int i = pStart; i <= pTail; i++) {
            if (!MessageInfo_list.get(i).is_confirm)
                return i;

        }
        return pTail;
    }

    public boolean Is_repeat(int ID)//判断是否是重复报文段
    {
        if (ID>MessageInfo_list.size()){
            return false;
        }
        return MessageInfo_list.get(ID).is_confirm;
    }


    public void Insert_Message(byte[] messageBytes, Time time)//插入报文段
    {
        int ID = RecieverDataProcessor.getMessageId(messageBytes);
        if(ID>MessageInfo_list.size())
        {
            ArrayList<RecieverOriginMessage> list=new ArrayList<>(ID+5);
            for (int i=0;i<MessageInfo_list.size();i++) {
                list.set(i, MessageInfo_list.get(i));
            }
        }
        MessageInfo_list.get(ID).dataLength=RecieverDataProcessor.getLength(messageBytes);
        MessageInfo_list.get(ID).is_confirm=true;
        MessageInfo_list.get(ID).data=RecieverDataProcessor.getData(messageBytes);
        MessageInfo_list.get(ID).Recieve_Time=time;
    }



    public static void Reciever_send_to_Sender(byte[] msg) throws IOException {
        recieverProcess.ackOutputStream.writeObject(msg);    // 向输出流中写入对象
        recieverProcess.ackOutputStream.flush(); // 务必flush！
    }

    public static void sendMessageToSender(RecieverACKMessage message) throws IOException {
        int index=message.ackId;
        byte[] ack= message.toByte();
        System.out.println("Receiver：senderProcess发送报文：" + index);
        Reciever_send_to_Sender(ack);

    }



    public void show_error()
    {

    }
    public void show()
    {

    }



}


