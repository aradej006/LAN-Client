/*
 * Created by JFormDesigner on Mon May 16 13:56:37 CEST 2016
 */

package com.pw.lan.client;

import com.pw.lan.client.client.Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Adrian Radej
 */
public class MainWindow extends JFrame {

    private MainWindow mainWindow;
    private String ipAddress;
    private String port;
    private String login;
    private String password;
    private Client client;

    public MainWindow() {
        mainWindow = this;
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void networkMenuActionPerformed(ActionEvent e) {
        NetworkWindow networkWindow = new NetworkWindow(this, "network");
    }

    private void connectBtnActionPerformed(ActionEvent e) {
        if(connectBtn.getText().equals("Connect")){
            try {
                client = new Client(ipAddress, port);
                connectBtn.setText("Disconnect");
                loginBtn.setEnabled(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else{
            client.forceLogout();
            client = null;
            connectBtn.setText("Connect");
            loginBtn.setEnabled(false);
        }
    }

    private void loginBtnActionPerformed(ActionEvent e) {
        if(client.login(login,password)){
            loginBtn.setEnabled(false);
        }
    }

    private void credentialsActionPerformed(ActionEvent e) {
        NetworkWindow networkWindow = new NetworkWindow(this, "credentials");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        menuBar1 = new JMenuBar();
        optionsMenu = new JMenu();
        networkMenu = new JMenuItem();
        credentials = new JMenuItem();
        panel2 = new JPanel();
        connectBtn = new JButton();
        loginBtn = new JButton();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== optionsMenu ========
            {
                optionsMenu.setText("Options");

                //---- networkMenu ----
                networkMenu.setText("Network");
                networkMenu.addActionListener(e -> networkMenuActionPerformed(e));
                optionsMenu.add(networkMenu);

                //---- credentials ----
                credentials.setText("Credentials");
                credentials.addActionListener(e -> credentialsActionPerformed(e));
                optionsMenu.add(credentials);
            }
            menuBar1.add(optionsMenu);
        }
        setJMenuBar(menuBar1);

        //======== panel2 ========
        {

            // JFormDesigner evaluation mark
            panel2.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), panel2.getBorder())); panel2.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            panel2.setLayout(new FlowLayout());

            //---- connectBtn ----
            connectBtn.setText("Connect");
            connectBtn.addActionListener(e -> connectBtnActionPerformed(e));
            panel2.add(connectBtn);

            //---- loginBtn ----
            loginBtn.setText("Login");
            loginBtn.setEnabled(false);
            loginBtn.addActionListener(e -> loginBtnActionPerformed(e));
            panel2.add(loginBtn);
        }
        contentPane.add(panel2, BorderLayout.NORTH);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(tree1);
        }
        contentPane.add(scrollPane1, BorderLayout.WEST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Adrian Radej
    private JMenuBar menuBar1;
    private JMenu optionsMenu;
    private JMenuItem networkMenu;
    private JMenuItem credentials;
    private JPanel panel2;
    private JButton connectBtn;
    private JButton loginBtn;
    private JScrollPane scrollPane1;
    private JTree tree1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
