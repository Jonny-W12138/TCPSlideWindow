package org.example.Reciever;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.example.Sender.SenderMessage;
import org.example.Sender.SenderProcess;
import sun.plugin2.message.Message;
import org.example.Sender.SenderDataProcessor;
import java.util.ArrayList;

// 接收窗口类
public class RecieverWindow{

    public class Message {
        int length;
        String data;
        boolean is_confirm;
    }
    int message_sum=0;


    private int pStart;//起始指针
    private int pTail;//尾部指针
    private Message[] MessageInfo_list;

    public RecieverWindow() {
        pStart = 0;
        pTail = 0;
        MessageInfo_list = new Message[1024];


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
            if (!MessageInfo_list[i].is_confirm)
                return i;
        }
        return pTail + 1;
    }

    public boolean Is_repeat(byte[] messageBytes) {
        int ID = ((messageBytes[1] & 0xFF) << 8) | (messageBytes[2] & 0xFF);
        return MessageInfo_list[ID].is_confirm;
    }


    public void Insert_Message(byte[] messageBytes)
    {
        int ID = SenderDataProcessor.getMessageId(messageBytes);
        MessageInfo_list[ID].length=SenderDataProcessor.getLength(messageBytes);
        MessageInfo_list[ID].is_confirm=true;
        MessageInfo_list[ID].data=SenderDataProcessor.getData(messageBytes);
        message_sum=message_sum+1;
    }




}


