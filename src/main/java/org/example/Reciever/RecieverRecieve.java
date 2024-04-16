package org.example.Reciever;
import java.util.Random;
// 报文段接收线程类
public class RecieverRecieve {
    private RecieverProcess rp;
    private RecieverWindow rw;



    public int get_error()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(4); // 生成一个0到3之间的随机数
        return randomNumber;
    }

}
