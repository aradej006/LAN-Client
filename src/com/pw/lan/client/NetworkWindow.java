/*
 * Created by JFormDesigner on Mon May 16 13:57:05 CEST 2016
 */

package com.pw.lan.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Adrian Radej
 */
public class NetworkWindow extends JFrame {

    private MainWindow mainWindow;
    private String frameName;

    public NetworkWindow(MainWindow mainWindow, String frameName) {
        this.mainWindow = mainWindow;
        this.frameName = frameName;
        initComponents();
        if(frameName.equals("credentials")){
            label1.setText("Login");
            label2.setText("Password");
            jPasswordField = new JPasswordField();
            panel2.remove(portField);
            panel2.add(jPasswordField, BorderLayout.SOUTH);

        }
        setVisible(true);
    }

    private void saveBtnActionPerformed(ActionEvent e) {
        if(!frameName.equals("credentials")){
            mainWindow.setIpAddress(ipAddressField.getText());
            mainWindow.setPort(portField.getText());
        } else{
            mainWindow.setLogin(ipAddressField.getText());
            mainWindow.setPassword(jPasswordField.getText());
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        saveBtn = new JButton();
        panel1 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        panel2 = new JPanel();
        ipAddressField = new JTextField();
        portField = new JTextField();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- saveBtn ----
        saveBtn.setText("Save");
        saveBtn.addActionListener(e -> saveBtnActionPerformed(e));
        contentPane.add(saveBtn, BorderLayout.SOUTH);

        //======== panel1 ========
        {

            // JFormDesigner evaluation mark
            panel1.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            panel1.setLayout(new BorderLayout());

            //---- label1 ----
            label1.setText("IP address");
            panel1.add(label1, BorderLayout.NORTH);

            //---- label2 ----
            label2.setText("Port");
            panel1.add(label2, BorderLayout.SOUTH);
        }
        contentPane.add(panel1, BorderLayout.WEST);

        //======== panel2 ========
        {
            panel2.setLayout(new BorderLayout());

            //---- ipAddressField ----
            ipAddressField.setPreferredSize(new Dimension(200, 31));
            panel2.add(ipAddressField, BorderLayout.NORTH);

            //---- portField ----
            portField.setText("10000");
            panel2.add(portField, BorderLayout.SOUTH);
        }
        contentPane.add(panel2, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Adrian Radej
    private JButton saveBtn;
    private JPanel panel1;
    private JLabel label1;
    private JLabel label2;
    private JPanel panel2;
    private JTextField ipAddressField;
    private JTextField portField;
    JPasswordField jPasswordField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
