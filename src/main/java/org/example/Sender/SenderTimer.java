package org.example.Sender;

import java.io.IOException;
import java.time.LocalTime;

//超时扫描线程类
public class SenderTimer extends Thread {
    SenderProcess senderProcess;
    SenderWindow senderWindow;

    public SenderTimer(SenderProcess sp, SenderWindow sw){
        senderProcess = sp;
        senderWindow = sw;
    }

    public void run(){
        System.out.println("SenderTimer开始运行！");
        while (true){
            if(!senderWindow.senderWindowList.isEmpty()){
                // 只要窗口中有元素 就遍历
                senderProcess.lock.lock();
                for (int i = senderWindow.pStart; i <= senderWindow.pTail; i++) {
                    SenderMessage message = senderWindow.senderWindowList.get(i);
                    if (message!=null && message.ifSended && !message.ifRecieverConfirmed) {
                        long currentTime = System.currentTimeMillis();
                        long sendTime = message.sendTime.getTime();
                        if (currentTime - sendTime > 10000) {    // 超过10s 认为超时
                            System.out.println("Sender超时器：" + i + "已超时");
                            SenderProcess.logDisplay += "[" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + "." + String.format("%03d", LocalTime.now().getNano() / 1_000_000) + "]" +  "Sender超时器：" + i + "已超时\n";
                            try {
                                senderWindow.sendMessageToReciever(i);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                senderProcess.lock.unlock();
            }
            try {
                Thread.sleep(9000); // 等待500毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
