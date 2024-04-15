package org.example.Sender;

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
                for (int i = 0; i < senderWindow.senderWindowList.size(); i++) {
                    SenderMessage message = senderWindow.senderWindowList.get(i);
                    if (message.ifSended && !message.ifRecieverConfirmed) {
                        long currentTime = System.currentTimeMillis();
                        long sendTime = message.sendTime.getTime();
                        if (currentTime - sendTime > 1000) {    // 超过1s 认为超时
                            System.out.println("Sender超时器：" + i + "已超时：" + message);
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
