package org.example.Reciever;

import org.example.Sender.SenderMessage;

import java.io.IOException;

// 累计确认计时器扫描线程类
public class RecieverConfirm {

    private RecieverWindow rw;
    private int max_send_time=9000;
    public int receive_time;



    public void run() throws IOException {
        System.out.println("ReceiverTimer开始运行！");
        while (true){
            if(!rw.is_empty()){
                // 只要窗口中有元素 就遍历
                for (int i = 0; i <rw.MessageInfo_list.size(); i++) {
                    RecieverOriginMessage message = rw.MessageInfo_list.get(i);
                    if (message. dataLength!=0&& !message.is_confirm) {
                        long currentTime = System.currentTimeMillis();
                        long recieve_time= message.Recieve_Time.getTime();
                        if (currentTime - recieve_time > 9000) {    // 超过1s 认为超时
                            System.out.println("Reciever超时器：" + i + "已超时：" + message);
                            RecieverACKMessage ack_message = new RecieverACKMessage(i);
                            RecieverWindow.sendMessageToSender(ack_message);
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
