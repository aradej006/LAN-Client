/*
 * Created by JFormDesigner on Mon May 16 13:57:05 CEST 2016
 */

package com.pw.lan.client;

import javax.swing.border.*;
import com.pw.lan.client.client.NetworkInformation;

import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

/**
 * @author Adrian Radej
 */
public class NetworkWindow extends JFrame {

    private MainWindow mainWindow;
    private NetworkInformation networkInformation;

    public NetworkWindow(MainWindow mainWindow) {
        setTitle("Network Settings");
        initComponents();
        this.mainWindow = mainWindow;
        networkInformation = mainWindow.getNetworkInformation();
        if(networkInformation!=null){
            ipAddressField.setText(networkInformation.getIpAddress());
            portField.setText(networkInformation.getPort());
            loginField.setText(networkInformation.getLogin());
            jPasswordField.setText(networkInformation.getPassword());
        }
        setLocationRelativeTo(mainWindow);
        setResizable(false);
        setVisible(true);

    }

    private void saveBtnActionPerformed(ActionEvent e) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(new String(jPasswordField.getPassword()).getBytes("UTF-8"));
            String password = String.format("%064x", new java.math.BigInteger(1, md.digest()));
            if(networkInformation.getPassword().equals(new String(jPasswordField.getPassword()))){
                mainWindow.setNetworkInformation(new NetworkInformation(ipAddressField.getText(),portField.getText(),loginField.getText(),networkInformation.getPassword()));
            }else{
                mainWindow.setNetworkInformation(new NetworkInformation(ipAddressField.getText(),portField.getText(),loginField.getText(),password));

            }
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            mainWindow.setNetworkWindowOpened(false);
            mainWindow.setEnabled(true);
            mainWindow.setFocusable(true);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


    }

    private void connectBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void thisWindowLostFocus(WindowEvent e) {
        this.toFront();
        this.requestFocus();
        this.requestFocusInWindow();
    }

    private void thisWindowClosing(WindowEvent e) {
        mainWindow.setNetworkWindowOpened(false);
        mainWindow.setEnabled(true);
        mainWindow.setFocusable(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        rootPane = new JPanel();
        credentialsPane = new JPanel();
        loginPane = new JPanel();
        loginLbl = new JLabel();
        loginField = new JTextField();
        passwordPane = new JPanel();
        passwordLbl = new JLabel();
        jPasswordField = new JPasswordField();
        buttonsPane = new JPanel();
        saveBtn = new JButton();
        networkPane = new JPanel();
        ipAddressPane = new JPanel();
        ipAddresLbl = new JLabel();
        ipAddressField = new JTextField();
        portPane = new JPanel();
        portLbl = new JLabel();
        portField = new JTextField();

        //======== this ========
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                thisWindowLostFocus(e);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== rootPane ========
        {

            // JFormDesigner evaluation mark
//            rootPane.setBorder(new javax.swing.border.CompoundBorder(
//                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
//                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
//                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
//                    java.awt.Color.red), rootPane.getBorder())); rootPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            rootPane.setLayout(new BorderLayout());

            //======== credentialsPane ========
            {
                credentialsPane.setBorder(new TitledBorder("Credentials"));
                credentialsPane.setLayout(new BorderLayout());

                //======== loginPane ========
                {
                    loginPane.setLayout(new BorderLayout());

                    //---- loginLbl ----
                    loginLbl.setText("Login");
                    loginPane.add(loginLbl, BorderLayout.NORTH);

                    //---- loginField ----
                    loginField.setPreferredSize(new Dimension(200, 22));
                    loginPane.add(loginField, BorderLayout.SOUTH);
                }
                credentialsPane.add(loginPane, BorderLayout.NORTH);

                //======== passwordPane ========
                {
                    passwordPane.setLayout(new BorderLayout());

                    //---- passwordLbl ----
                    passwordLbl.setText("Password");
                    passwordPane.add(passwordLbl, BorderLayout.NORTH);

                    //---- jPasswordField ----
                    jPasswordField.setPreferredSize(new Dimension(200, 22));
                    passwordPane.add(jPasswordField, BorderLayout.SOUTH);
                }
                credentialsPane.add(passwordPane, BorderLayout.SOUTH);
            }
            rootPane.add(credentialsPane, BorderLayout.EAST);

            //======== buttonsPane ========
            {
                buttonsPane.setLayout(new BorderLayout());

                //---- saveBtn ----
                saveBtn.setText("Save");
                saveBtn.addActionListener(e -> {
			connectBtnActionPerformed(e);
			saveBtnActionPerformed(e);
		});
                buttonsPane.add(saveBtn, BorderLayout.EAST);
            }
            rootPane.add(buttonsPane, BorderLayout.SOUTH);

            //======== networkPane ========
            {
                networkPane.setBorder(new TitledBorder("Network"));
                networkPane.setLayout(new BorderLayout());

                //======== ipAddressPane ========
                {
                    ipAddressPane.setLayout(new BorderLayout());

                    //---- ipAddresLbl ----
                    ipAddresLbl.setText("IP address");
                    ipAddressPane.add(ipAddresLbl, BorderLayout.NORTH);

                    //---- ipAddressField ----
                    ipAddressField.setPreferredSize(new Dimension(200, 22));
                    ipAddressPane.add(ipAddressField, BorderLayout.SOUTH);
                }
                networkPane.add(ipAddressPane, BorderLayout.NORTH);

                //======== portPane ========
                {
                    portPane.setLayout(new BorderLayout());

                    //---- portLbl ----
                    portLbl.setText("Port");
                    portPane.add(portLbl, BorderLayout.NORTH);

                    //---- portField ----
                    portField.setText("10000");
                    portField.setPreferredSize(new Dimension(200, 22));
                    portPane.add(portField, BorderLayout.SOUTH);
                }
                networkPane.add(portPane, BorderLayout.SOUTH);
            }
            rootPane.add(networkPane, BorderLayout.WEST);
        }
        contentPane.add(rootPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Adrian Radej
    private JPanel rootPane;
    private JPanel credentialsPane;
    private JPanel loginPane;
    private JLabel loginLbl;
    private JTextField loginField;
    private JPanel passwordPane;
    private JLabel passwordLbl;
    private JPasswordField jPasswordField;
    private JPanel buttonsPane;
    private JButton saveBtn;
    private JPanel networkPane;
    private JPanel ipAddressPane;
    private JLabel ipAddresLbl;
    private JTextField ipAddressField;
    private JPanel portPane;
    private JLabel portLbl;
    private JTextField portField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
