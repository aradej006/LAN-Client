/*
 * Created by JFormDesigner on Mon May 16 13:56:37 CEST 2016
 */

package com.pw.lan.client;

import com.pw.lan.client.client.Client;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author Adrian Radej
 */
public class MainWindow extends JFrame {

    private String ipAddress;
    private String port;
    private String login;
    private String password;
    private Client client;

    public MainWindow() {
        super("LAN Client App");
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void networkMenuActionPerformed(ActionEvent e) {
        new NetworkWindow(this, "network");
    }

    private void connectBtnActionPerformed(ActionEvent e) {
        if(connectBtn.getText().equals("Connect")){
            try {
                client = new Client(ipAddress, port);
                client.setMainWindow(this);
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
        new NetworkWindow(this, "credentials");
    }

    public void updateJTree(String filesPath, Map<String,String> files){
        String[] splited = filesPath.split("/");
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node = root;
        root.setUserObject("root");
        if(splited.length==1) root.removeAllChildren();
        for(int i=1;i<splited.length;i++){
            Enumeration childern = root.children();
            while(childern.hasMoreElements()){
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)childern.nextElement();
                if(n.getUserObject().equals(splited[i])){
                    node = n;
                    break;
                }
            }
        }
        for (String key : files.keySet()) {
            node.add(new DefaultMutableTreeNode(key));
            //// TODO: 2016-05-17 add items to directories
        }

        model.reload(root);
    }

    private void button2ActionPerformed(ActionEvent e) {
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.setUserObject("root");
        root.removeAllChildren();
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("folder1");
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("folder2");
        DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("file1");
        DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("file2");
        DefaultMutableTreeNode node5 = new DefaultMutableTreeNode("file3");
        root.add(node1);
        root.add(node2);
        root.add(node5);
        node1.add(node3);
        node1.add(node4);
        model.reload(root);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        menuBar1 = new JMenuBar();
        optionsMenu = new JMenu();
        networkMenu = new JMenuItem();
        credentials = new JMenuItem();
        panel2 = new JPanel();
        panel5 = new JPanel();
        connectBtn = new JButton();
        loginBtn = new JButton();
        scrollPane1 = new JScrollPane();
        fileTree = new JTree();
        propPanel = new JPanel();
        label1 = new JLabel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        button2 = new JButton();
        infoPanel = new JPanel();
        actionLbl = new JLabel();
        statusLbl = new JLabel();

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

            panel2.setLayout(new BorderLayout());

            //======== panel5 ========
            {
                panel5.setLayout(new BorderLayout());

                //---- connectBtn ----
                connectBtn.setText("Connect");
                connectBtn.addActionListener(e -> connectBtnActionPerformed(e));
                panel5.add(connectBtn, BorderLayout.WEST);

                //---- loginBtn ----
                loginBtn.setText("Login");
                loginBtn.setEnabled(false);
                loginBtn.addActionListener(e -> loginBtnActionPerformed(e));
                panel5.add(loginBtn, BorderLayout.EAST);
            }
            panel2.add(panel5, BorderLayout.WEST);
        }
        contentPane.add(panel2, BorderLayout.NORTH);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(fileTree);
        }
        contentPane.add(scrollPane1, BorderLayout.CENTER);

        //======== propPanel ========
        {
            propPanel.setLayout(new BorderLayout());

            //---- label1 ----
            label1.setText("Properties");
            propPanel.add(label1, BorderLayout.NORTH);

            //======== panel3 ========
            {
                panel3.setLayout(new BorderLayout());
            }
            propPanel.add(panel3, BorderLayout.WEST);

            //======== panel4 ========
            {
                panel4.setLayout(new BorderLayout());
            }
            propPanel.add(panel4, BorderLayout.EAST);

            //---- button2 ----
            button2.setText("text");
            button2.addActionListener(e -> button2ActionPerformed(e));
            propPanel.add(button2, BorderLayout.SOUTH);
        }
        contentPane.add(propPanel, BorderLayout.EAST);

        //======== infoPanel ========
        {
            infoPanel.setLayout(new BorderLayout());

            //---- actionLbl ----
            actionLbl.setText("Action");
            infoPanel.add(actionLbl, BorderLayout.EAST);

            //---- statusLbl ----
            statusLbl.setText("Status");
            infoPanel.add(statusLbl, BorderLayout.WEST);
        }
        contentPane.add(infoPanel, BorderLayout.SOUTH);
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
    private JPanel panel5;
    private JButton connectBtn;
    private JButton loginBtn;
    private JScrollPane scrollPane1;
    private JTree fileTree;
    private JPanel propPanel;
    private JLabel label1;
    private JPanel panel3;
    private JPanel panel4;
    private JButton button2;
    private JPanel infoPanel;
    private JLabel actionLbl;
    private JLabel statusLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
