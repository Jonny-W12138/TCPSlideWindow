package org.example.Reciever;

import org.example.Sender.SenderMessage;

import java.io.IOException;

// 累计确认计时器扫描线程类
public class RecieverConfirm extends Thread {

    private RecieverWindow rw;
    private int max_send_time = 9000;
    public int receive_time;

    public RecieverConfirm(RecieverWindow rw) {
        this.rw = rw;
    }


    public void run() {
        System.out.println("ReceiverTimer开始运行！");
        while (true) {
            if (!rw.is_empty()) {
                // 只要窗口中有元素 就遍历
                for (int i = 0; i < rw.MessageInfo_list.size(); i++) {
                    RecieverOriginMessage message = rw.MessageInfo_list.get(i);
                    if (message.dataLength != 0 && !message.is_confirm) {
                        long currentTime = System.currentTimeMillis();
                        long recieve_time = message.Recieve_Time.getTime();
                        if (currentTime - recieve_time > 1000) {    // 超过1s 认为超时
                            System.out.println("Reciever超时器：" + i + "已超时：" + message);
                            RecieverProcess.textDisplay += "Reciever超时器：" + i + "已超时：" + message + "\n";
                            RecieverACKMessage ack_message = new RecieverACKMessage(rw.Get_IDmax());
                            try {
                                RecieverWindow.sendMessageToSender(ack_message);
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
