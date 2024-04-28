package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class Test {
    private JPanel panel1;
    private JTable table1;
    private JScrollBar scrollBar1;

    public Test() {
        DefaultTableModel model = new DefaultTableModel();

        JTableHeader header = table1.getTableHeader();
        header.setVisible(false);
        for(int i=0;i<12;++i){
            model.addColumn("");
        }
        model.addRow(new Object[]{"Alice", 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30});
        table1.setModel(model);

        for(int i=0;i<12;++i){
            TableColumn column = table1.getColumnModel().getColumn(i);
            column.setMinWidth(100);
            column.setMaxWidth(100);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new Test().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
