package org.example.Receiver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.io.IOException;

public class Receiver {
    private JTextField portNum;
    private JTextField senderWindowSize;
    private JButton 关闭Button;
    public JPanel Receiver;
    private JTextArea receiverLog;
    private JPanel ptrPanel;
    private JLabel pStartLabel;
    private JTextField pStart;
    private JLabel pTailLabel;
    private JTextField pTail;
    private JScrollBar scrollBar1;
    private JButton modifySenderBtn;
    private ReceiverWindow receiverWindow;

    public Receiver(ReceiverWindow rw) {
        receiverWindow = rw;

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
        final String[] preText = {ReceiverProcess.textDisplay};
        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the text field with the current value of the variable
                if(!ReceiverProcess.textDisplay.equals(preText[0])){
                    receiverLog.setText(ReceiverProcess.textDisplay);
                    preText[0] = ReceiverProcess.textDisplay;
                }
                pStart.setText(String.valueOf(receiverWindow.get_pStart()));
                pTail.setText(String.valueOf(receiverWindow.get_pTail()));
                portNum.setText(String.valueOf(ReceiverReceive.portNum));
            }
        });
        timer.start();
        modifySenderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReceiverProcess.newWindowSize = Integer.parseInt(senderWindowSize.getText());
                System.out.println("【Debug】新的窗口大小为："+ReceiverProcess.newWindowSize);
                ReceiverACKMessage ank_message = new ReceiverACKMessage(-1,ReceiverProcess.newWindowSize);
                try {
                    ReceiverWindow.sendMessageToSender(ank_message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        关闭Button.addKeyListener(new KeyAdapter() {
        });
        关闭Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
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
