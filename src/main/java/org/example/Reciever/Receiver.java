package org.example.Reciever;

import javax.swing.*;

public class Receiver {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton 关闭Button;
    private JTable table1;
    public JPanel Receiver;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Receiver");
        frame.setContentPane(new Receiver().Receiver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
