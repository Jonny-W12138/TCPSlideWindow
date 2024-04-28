package org.example.Reciever;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Receiver {
    private JTextField textField1;
    private JTextField textField2;
    private JButton 关闭Button;
    private JTable table1;
    public JPanel Receiver;
    private JTextArea recieverLog;
    private JPanel ptrPanel;
    private JLabel pStartLabel;
    private JTextField pStart;
    private JLabel pTailLabel;
    private JTextField pTail;
    private RecieverWindow recieverWindow;

    public Receiver(RecieverWindow rw) {
        recieverWindow = rw;
        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the text field with the current value of the variable
                recieverLog.setText(RecieverProcess.textDisplay);
                pStart.setText(String.valueOf(recieverWindow.get_pStart()));
                pTail.setText(String.valueOf(recieverWindow.get_pTail()));
            }
        });
        timer.start();
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
