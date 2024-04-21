package org.example.Sender;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Sender {
    JPanel panel1;
    private JButton closeBtn;
    private JTextArea senderLog;
    private JTextField portNumber;
    private JTextField IPAddr;
    private JTextField sendText;
    private JTextField sendTimes;
    private JButton sendBtn;
    private JPanel recieveInfoPanel;
    private JPanel sendTextPanel;
    private JPanel sendTimePanel;
    private JLabel SendTextLabel;
    private JTable table1;
    public String logContext;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sender");
        frame.setContentPane(new Sender().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Sender() {
        sendText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SenderProcess.textToSend = sendText.getText();
                //System.out.println("发送窗体：" + SenderProcess.textToSend);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SenderProcess.textToSend = sendText.getText();
                //System.out.println("发送窗体：" + SenderProcess.textToSend);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SenderProcess.textToSend = sendText.getText();
                //System.out.println("发送窗体：" + SenderProcess.textToSend);
            }
        });
        IPAddr.setText(SenderProcess.getRecieverIP());
        System.out.println("接收端ip：" + SenderProcess.getRecieverIP());
        portNumber.setText(Integer.toString(SenderProcess.getRecieverPort()));
        System.out.println("接收端端口：" + SenderProcess.getRecieverPort());
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SenderProcess.senderAddMessage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the text field with the current value of the variable
                senderLog.setText(logContext);
            }
        });
        timer.start(); // Start the timer
        while (true) {}
    }


    public void main() {

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
