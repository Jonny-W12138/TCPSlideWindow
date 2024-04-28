package org.example.Receiver;

import java.io.IOException;

// 累计确认计时器扫描线程类
public class ReceiverConfirm extends Thread {

    private ReceiverWindow rw;
    private int max_send_time = 9000;
    public int receive_time;

    public ReceiverConfirm(ReceiverWindow rw) {
        this.rw = rw;
    }


    public void run() {
        System.out.println("ReceiverTimer开始运行！");
        while (true) {
            if (!rw.is_empty()) {
                // 只要窗口中有元素 就遍历
                for (int i = 0; i < rw.MessageInfo_list.size(); i++) {
                    ReceiverOriginMessage message = rw.MessageInfo_list.get(i);
                    /*if(message!=null){
                        System.out.println("Receiver的map长度："+rw.MessageInfo_list.size());
                        System.out.println("ReceiverConfirm遍历到: "+message.messageId+"，是否确认："+message.is_confirm);
                    }*/
                    if (message!=null && message.dataLength != 0 && !message.is_confirm) {
                        long currentTime = System.currentTimeMillis();
                        long receive_time = message.Receive_Time.getTime();
                        if (currentTime - receive_time > 9000) {    // 超过9s 认为超时
                            System.out.println("Receiver超时器：" + i + "已超时");
                            ReceiverProcess.textDisplay += "Receiver超时器：" + i + "已超时\n";
                            ReceiverACKMessage ack_message = new ReceiverACKMessage(rw.Get_IDmax());
                            try {
                                ReceiverWindow.sendMessageToSender(ack_message);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
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
