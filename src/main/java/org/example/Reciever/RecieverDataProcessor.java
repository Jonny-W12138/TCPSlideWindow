package org.example.Reciever;

// 数据封装类
public class RecieverDataProcessor {
    private int dataLength;//数据长度
    private int messageId;// 报文标识
    private byte[] dataBytes;// 报文内容

    public int getMessageId()
    {
        return messageId;
    }


    public RecieverDataProcessor(byte[] messageBytes)
    {
        int highByte = (messageBytes[1] & 0xFF); // 提取高位字节
        int lowByte = (messageBytes[2] & 0xFF);  // 提取低位字节
        messageId=(highByte << 8) | lowByte;
        highByte = messageBytes[3] & 0xFF; // 提取高位字节
        lowByte = messageBytes[4] & 0xFF;  // 提取低位字节
        int dataLength = (highByte << 8) | lowByte; // 合并两个字节得到数据长度


        dataBytes = new byte[dataLength];
        System.arraycopy(messageBytes, 5, dataBytes, 0, dataLength); // 从索引5开始复制数据部分
    }











}
