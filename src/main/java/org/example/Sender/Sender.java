package org.example.Sender;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
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
    private JTable SenderTable;
    private JPanel windowStatus;
    private JLabel pStartLabel;
    private JTextField pStart;
    private JLabel pCurLabel;
    private JTextField pCur;
    private JLabel pTailLabel;
    private JTextField pTail;
    private JLabel windowSizeLabel;
    private JTextField windowSize;
    private JScrollBar scrollBar1;
    private JScrollPane ScrollLog;
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

                //System.out.println("发送窗体：" + SenderProcess.textToSend);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SenderProcess.textToSend = sendText.getText();
                //System.out.println("发送窗体：" + SenderProcess.textToSend);
            }
        });
        sendTimes.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SenderProcess.textSendTimes = Integer.parseInt(sendTimes.getText());
                //System.out.println("发送次数：" + SenderProcess.sendTimes);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!sendTimes.getText().equals("")) {
                    SenderProcess.textSendTimes = Integer.parseInt(sendTimes.getText());
                }
                //System.out.println("发送次数：" + SenderProcess.sendTimes);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SenderProcess.textSendTimes = Integer.parseInt(sendTimes.getText());
                //System.out.println("发送次数：" + SenderProcess.sendTimes);
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

        final String[] preLog = {SenderProcess.logDisplay};
        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the text field with the current value of the variable
                if(!preLog[0].equals(SenderProcess.logDisplay)){
                    senderLog.setText(SenderProcess.logDisplay);
                    preLog[0] = SenderProcess.logDisplay;
                }
                pCur.setText(Integer.toString(SenderWindow.getPCur()));
                pTail.setText(Integer.toString(SenderWindow.getPTail()));
                pStart.setText(Integer.toString(SenderWindow.getPStart()));
                windowSize.setText(Integer.toString(SenderWindow.getWindowSize()));

                /*for(int i=0;i<SenderWindow.elementNum;i++){
                    TableColumn newColumn = new TableColumn(i);
                    SenderTable.addColumn(newColumn);
                }*/
            }

        });
        timer.start(); // Start the timer

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
            }
        });
    }


    public void main() {

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
