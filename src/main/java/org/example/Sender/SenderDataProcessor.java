package org.example.Sender;

import java.util.Arrays;

// 数据封装类
public class SenderDataProcessor {
    public static byte[] convertMessage(String content, int messageId) {
        SenderOriginMessage message = new SenderOriginMessage(messageId, content);
        byte[] messageBytes = message.toBytes();

        // 计算校验和
        byte[] checksum = calculateChecksum(messageBytes);

        // 将校验和追加到消息末尾
        byte[] result = new byte[messageBytes.length + checksum.length];
        System.arraycopy(messageBytes, 0, result, 0, messageBytes.length);
        System.arraycopy(checksum, 0, result, messageBytes.length, checksum.length);

        return result;
    }

    private static byte[] calculateChecksum(byte[] data) {
        int checksumValue = 0;
        int carry = 0;

        // 对每16位进行计算
        for (int i = 0; i < data.length; i++) {
            checksumValue += (data[i] & 0xFF) << 8; // 8位数据左移8位
            if ((checksumValue & 0xFFFF0000) != 0) { // 判断是否有进位
                checksumValue &= 0xFFFF; // 保留低16位
                checksumValue++; // 进位加1
            }

            i++;
            if (i < data.length) { // 如果还有数据
                checksumValue += (data[i] & 0xFF);
                if ((checksumValue & 0xFFFF0000) != 0) {    // 判断是否有进位
                    checksumValue &= 0xFFFF;    // 保留低16位
                    checksumValue++;    // 进位加1
                }
            }
        }

        // 取反得到校验和
        checksumValue = ~checksumValue & 0xFFFF;

        // 将校验和值转换为字节数组
        byte[] checksumBytes = new byte[2];
        checksumBytes[0] = (byte) (checksumValue >> 8);
        checksumBytes[1] = (byte) (checksumValue & 0xFF);

        return checksumBytes;
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
