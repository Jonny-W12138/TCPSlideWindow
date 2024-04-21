package org.example.Reciever;

import org.example.Sender.SenderMessage;

import java.io.IOException;

// 累计确认计时器扫描线程类
public class RecieverConfirm {

    private RecieverWindow rw;
    private int max_send_time=9000;
    public int receive_time;



    public void Confirm_cumulative_time_limit_scan()
    {
        int p1=rw.get_pStart();
        int p2=rw.get_pTail();
        RecieverWindow.Message[]message_segmentation=new RecieverWindow.Message[4];
        int i,j;
        boolean is_empty=true;
        for (i=p1,j=0;i<=p2;i++)
        {
            message_segmentation[j]=rw.get_MessageInfo_list().get(i);
            if(message_segmentation[j].is_confirm)//通过是否确认判断接收窗口中的报文段信息列表是否为空
            {
                is_empty=false;
            }
        }
        if (!is_empty)
        {
            long now_time=System.currentTimeMillis();//获得当前时间
            if(now_time-receive_time>max_send_time)
            {
                rw.operate();
            }
            else
            {
                rw.show();;
            }
        }
        else
        {
            rw.show();
        }





    }


    public void run(){
        System.out.println("ReceiverTimer开始运行！");
        while (true){
            if(!rw.is_empty()){
                // 只要窗口中有元素 就遍历
                for (int i = 0; i <rw.MessageInfo_list.size(); i++) {
                    RecieverWindow.Message message = rw.MessageInfo_list.get(i);
                    if (message. && !message.ifRecieverConfirmed) {
                        long currentTime = System.currentTimeMillis();
                        long sendTime = message.sendTime.getTime();
                        if (currentTime - sendTime > 1000) {    // 超过1s 认为超时
                            System.out.println("Sender超时器：" + i + "已超时：" + message);
                            try {
                                senderWindow.sendMessageToReciever(i);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

            }
            try {
                Thread.sleep(500); // 等待500毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
