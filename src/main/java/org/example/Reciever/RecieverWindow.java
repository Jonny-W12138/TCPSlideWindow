package org.example.Reciever;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.example.Sender.SenderMessage;
import org.example.Sender.SenderProcess;
import sun.plugin2.message.Message;
import org.example.Sender.SenderDataProcessor;
import java.util.ArrayList;
import org.example.Reciever.RecieverDataProcessor;
import org.example.Reciever.RecieverRecieve;
// 接收窗口类
public class RecieverWindow{


    int size=3;//窗口大小
    int message_sum=0;//报文累加器


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


    public RecieverWindow() {
        pStart = 0;
        pTail = 0;
        MessageInfo_list=new ArrayList<>();
    }

    public byte[] send_ack(int IDmax)//发送确认
    {
        byte[] messageBytes = new byte[3];
        messageBytes[0] = 1;
        messageBytes[1] = (byte) ((IDmax >> 8) & 0xFF); // 高位
        messageBytes[2] = (byte) (IDmax & 0xFF); // 低位
        return messageBytes;
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

    public boolean Is_repeat(int ID) {
        if (ID>MessageInfo_list.size()){
            return false;
        }
        return MessageInfo_list.get(ID).is_confirm;
    }


    public void Insert_Message(byte[] messageBytes)
    {
        int ID = RecieverDataProcessor.getMessageId(messageBytes);
        if(ID>MessageInfo_list.size())
        {
            ArrayList<Message> list=new ArrayList<>(ID+5);
            for (int i=0;i<MessageInfo_list.size();i++) {
                list.set(i, MessageInfo_list.get(i));
            }
        }
        MessageInfo_list.get(ID).datalength=RecieverDataProcessor.getLength(messageBytes);
        MessageInfo_list.get(ID).is_confirm=true;
        MessageInfo_list.get(ID).data=RecieverDataProcessor.getData(messageBytes);
    }

    void operate()
    {
        int IDmax=Get_IDmax();
        byte[]ACK=new byte[3];
        ACK=send_ack(IDmax+1);//确认消息
        Move(IDmax);
        message_sum=0;
    }

    public void process(byte[] messageBytes)//接收报文段处理流程
    {
        RecieverRecieve rr=new RecieverRecieve();
        RecieverDataProcessor rdp=new RecieverDataProcessor(messageBytes);
        int ID=rdp.getMessageId();//解析报文段,获得IDseg
        int error_type=rr.get_error();
        if(error_type==0)//正常接收
        {
            if(ID>pTail||ID<pStart)
            {
                show_error();
            }
            else
            {
                if(MessageInfo_list.get(ID).is_confirm)
                {
                    operate();
                    show();

                }
                else
                {
                    Insert_Message(messageBytes);
                    message_sum=message_sum+1;
                    if (message_sum==3)
                    {
                        operate();
                    }
                    show();
                }
            }

        }
        else if (error_type==1||error_type==2)
        {
            show_error();
        }
        else
        {

        }


    }

    public void show_error()
    {

    }
    public void show()
    {

    }



}


