package org.example.Sender;

import java.util.Arrays;

// 数据封装类
public class SenderDataProcessor {
    public static byte[] convertMessage(String content, int messageId) {
        SenderOriginMessage message = new SenderOriginMessage(messageId, content);
        return message.toBytes();
    }

    // 以下为测试用代码
    public static int getMessageId(byte[] messageBytes) {
        // 从字节数组中获取报文标识，占2个字节，高位在前低位在后
        return ((messageBytes[1] & 0xFF) << 8) | (messageBytes[2] & 0xFF);
    }

    public static int getLength(byte[] messageBytes) {
        // 获取数据长度，占2个字节，高位在前低位在后
        return ((messageBytes[3] & 0xFF) << 8) | (messageBytes[4] & 0xFF);
    }

    public static String getData(byte[] messageBytes) {
        // 获取数据长度，占2个字节，高位在前低位在后
        int dataLength = ((messageBytes[3] & 0xFF) << 8) | (messageBytes[4] & 0xFF);

        // 从字节数组中提取数据部分
        byte[] dataBytes = Arrays.copyOfRange(messageBytes, 5, 5 + dataLength);

        // 将数据部分转换为字符串
        return new String(dataBytes);
    }


    public static void main(String[] args) {
        String content = "Hello, World!";
        int messageId = 12345;
        byte[] messageBytes = convertMessage(content, messageId);

        System.out.println("解码 - 报文标识：" + getMessageId(messageBytes));
        System.out.println(("解码 - 原始长度：" + getLength(messageBytes)));
        System.out.println(("解码 - 原始字符串：" + getData(messageBytes)));
    }
}
