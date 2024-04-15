package org.example;

import org.example.Reciever.RecieverProcess;
import org.example.Sender.SenderProcess;

import java.io.IOException;

public class Start {
    static SenderProcess senderProcess;
    static RecieverProcess recieverProcess;

    public static void main(String[] args) throws IOException {
        senderProcess = new SenderProcess();
        recieverProcess = new RecieverProcess();

        senderProcess.start();
        recieverProcess.start();


    }
}
