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

    public NetworkWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
        setVisible(true);
    }

    private void saveBtnActionPerformed(ActionEvent e) {
        mainWindow.setIpAddress(ipAddressField.getText());
        mainWindow.setPort(portField.getText());
        mainWindow.setLogin(ipAddressField.getText());
        mainWindow.setPassword(jPasswordField.getText());
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void connectBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        saveBtn = new JButton();
        panel1 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        ipAddresLbl = new JLabel();
        portLbl = new JLabel();
        panel5 = new JPanel();
        ipAddressField = new JTextField();
        portField = new JTextField();
        panel2 = new JPanel();
        panel6 = new JPanel();
        panel7 = new JPanel();
        loginField = new JTextField();
        jPasswordField = new JPasswordField();
        panel8 = new JPanel();
        loginLbl = new JLabel();
        passwordLbl = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- saveBtn ----
        saveBtn.setText("Save");
        saveBtn.addActionListener(e -> {
			connectBtnActionPerformed(e);
			saveBtnActionPerformed(e);
		});
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

            //======== panel3 ========
            {
                panel3.setLayout(new BorderLayout());

                //======== panel4 ========
                {
                    panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));

                    //---- ipAddresLbl ----
                    ipAddresLbl.setText("IP address");
                    panel4.add(ipAddresLbl);

                    //---- portLbl ----
                    portLbl.setText("Port");
                    panel4.add(portLbl);
                }
                panel3.add(panel4, BorderLayout.WEST);

                //======== panel5 ========
                {
                    panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));

                    //---- ipAddressField ----
                    ipAddressField.setPreferredSize(new Dimension(200, 31));
                    panel5.add(ipAddressField);

                    //---- portField ----
                    portField.setText("10000");
                    panel5.add(portField);
                }
                panel3.add(panel5, BorderLayout.EAST);
            }
            panel1.add(panel3, BorderLayout.WEST);
        }
        contentPane.add(panel1, BorderLayout.CENTER);

        //======== panel2 ========
        {
            panel2.setLayout(new BorderLayout());

            //======== panel6 ========
            {
                panel6.setLayout(new BorderLayout());

                //======== panel7 ========
                {
                    panel7.setLayout(new BoxLayout(panel7, BoxLayout.Y_AXIS));

                    //---- loginField ----
                    loginField.setPreferredSize(new Dimension(200, 31));
                    panel7.add(loginField);
                    panel7.add(jPasswordField);
                }
                panel6.add(panel7, BorderLayout.EAST);

                //======== panel8 ========
                {
                    panel8.setLayout(new BoxLayout(panel8, BoxLayout.Y_AXIS));

                    //---- loginLbl ----
                    loginLbl.setText("Login");
                    panel8.add(loginLbl);

                    //---- passwordLbl ----
                    passwordLbl.setText("Password");
                    panel8.add(passwordLbl);
                }
                panel6.add(panel8, BorderLayout.WEST);
            }
            panel2.add(panel6, BorderLayout.CENTER);
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
    private JPanel panel3;
    private JPanel panel4;
    private JLabel ipAddresLbl;
    private JLabel portLbl;
    private JPanel panel5;
    private JTextField ipAddressField;
    private JTextField portField;
    private JPanel panel2;
    private JPanel panel6;
    private JPanel panel7;
    private JTextField loginField;
    private JPasswordField jPasswordField;
    private JPanel panel8;
    private JLabel loginLbl;
    private JLabel passwordLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
