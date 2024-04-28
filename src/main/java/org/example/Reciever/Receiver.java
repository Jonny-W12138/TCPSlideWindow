package org.example.Reciever;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Receiver {
    private JTextField portNum;
    private JTextField senderWindowSize;
    private JButton 关闭Button;
    private JTable table1;
    public JPanel Receiver;
    private JTextArea recieverLog;
    private JPanel ptrPanel;
    private JLabel pStartLabel;
    private JTextField pStart;
    private JLabel pTailLabel;
    private JTextField pTail;
    private JScrollBar scrollBar1;
    private JButton modifySenderBtn;
    private RecieverWindow recieverWindow;

    public Receiver(RecieverWindow rw) {
        recieverWindow = rw;

        senderWindowSize.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the text field with the current value of the variable
                recieverLog.setText(RecieverProcess.textDisplay);
                pStart.setText(String.valueOf(recieverWindow.get_pStart()));
                pTail.setText(String.valueOf(recieverWindow.get_pTail()));
                portNum.setText(String.valueOf(RecieverRecieve.portNum));
            }
        });
        timer.start();
        modifySenderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecieverProcess.newWindowSize = Integer.parseInt(senderWindowSize.getText());
                System.out.println("【Debug】新的窗口大小为："+RecieverProcess.newWindowSize);
                RecieverACKMessage ank_message = new RecieverACKMessage(-1,RecieverProcess.newWindowSize);
                try {
                    RecieverWindow.sendMessageToSender(ank_message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver().Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }*/

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver().Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }*/

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
