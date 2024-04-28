package org.example;

import org.example.Receiver.ReceiverProcess;
import org.example.Sender.SenderProcess;

import java.io.IOException;

public class Start {
    static SenderProcess senderProcess;
    static ReceiverProcess recieverProcess;

    public static void main(String[] args) throws IOException {
        senderProcess = new SenderProcess();
        recieverProcess = new ReceiverProcess();

        senderProcess.start();
        recieverProcess.start();


    }
}
