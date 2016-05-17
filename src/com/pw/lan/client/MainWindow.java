/*
 * Created by JFormDesigner on Mon May 16 13:56:37 CEST 2016
 */

package com.pw.lan.client;

import javax.swing.border.*;
import javax.swing.event.*;
import com.pw.lan.client.client.Client;
import com.pw.lan.client.tree.FileMutableTreeNode;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Adrian Radej
 */
public class MainWindow extends JFrame {

    private String ipAddress;
    private String port;
    private String login;
    private String password;
    private Client client;
    private boolean expandingFilesTree;

    public MainWindow() {
        super("LAN Client App");
        initComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        expandingFilesTree = false;

        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        root.setUserObject("root");
        model.reload(root);
        fileTree.setEnabled(false);
    }

    private void networkMenuActionPerformed(ActionEvent e) {
        new NetworkWindow(this);
    }

    private void connectBtnActionPerformed(ActionEvent e) {
        if(connectBtn.getText().equals("Connect")){
            try {
                connectBtn.setText("Connecting...");
                client = new Client(ipAddress, port);
                client.setMainWindow(this);
                connectBtn.setText("Disconnect");
                loginBtn.setEnabled(true);
                statusLbl.setText("Connected, Not Logged In");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else{
            client.forceLogout();
            client = null;
            connectBtn.setText("Connected");
            statusLbl.setText("Disconnected");
            loginBtn.setEnabled(false);
            fileTree.setEnabled(false);
        }
    }

    private void loginBtnActionPerformed(ActionEvent e) {
        statusLbl.setText("Connected, Logging In...");
        if(client.login(login,password)){
            loginBtn.setEnabled(false);
            statusLbl.setText("Connected, Log In");
        }
    }

    private void credentialsActionPerformed(ActionEvent e) {
        new NetworkWindow(this);
    }

    public void updateFilesTree(String filesPath, Map<String,String> files){
        String[] split = filesPath.split("/");
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node = root;
        root.setUserObject("root");
        if(split.length==1) root.removeAllChildren();
        for(int i=1;i<split.length;i++){
            Enumeration children = node.children();
            while(children.hasMoreElements()){
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)children.nextElement();
                if(n.getUserObject().equals(split[i])){
                    node = n;
                    if(i==split.length-1){
                        node.removeAllChildren();
                    }
                    break;
                }
            }
        }
        for (String key : files.keySet()) {
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(key);
            if(files.get(key).split(" ")[0].equals("dir")){
                n.add(new DefaultMutableTreeNode("<items>"));
            }else{
                n = new FileMutableTreeNode(key,files.get(key).split(" ")[0],Long.parseLong(files.get(key).split(" ")[1]));
            }
            node.add(n);
        }
        model.reload(root);
        fileTree.expandPath(new TreePath(node.getPath()));
        expandingFilesTree = false;
        actionLbl.setText("Fetched");
        fileTree.setEnabled(true);
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

    private void fileTreeTreeExpanded(TreeExpansionEvent e) {
        if( !expandingFilesTree ){
            String filesPath = e.getPath().toString().substring(1,e.getPath().toString().length()-1).replace(", ","/") + "/";
            client.getFiles(filesPath);
            expandingFilesTree = true;
            actionLbl.setText("Fetching...");
        }
    }

    private void fileTreeValueChanged(TreeSelectionEvent e) {
        if(fileTree.getLastSelectedPathComponent() instanceof FileMutableTreeNode){
            FileMutableTreeNode node = (FileMutableTreeNode) fileTree.getLastSelectedPathComponent();
            nameLbl.setText(node.getName());
            extensionLbl.setText(node.getExtension());
            sizeLbl.setText(node.getSize());
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        if(client!=null)
            client.forceLogout();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        menuBar1 = new JMenuBar();
        optionsMenu = new JMenu();
        networkMenu = new JMenuItem();
        buttonsPanel = new JPanel();
        panel5 = new JPanel();
        connectBtn = new JButton();
        loginBtn = new JButton();
        scrollPane1 = new JScrollPane();
        fileTree = new JTree();
        propPanel = new JPanel();
        propNamesPanel = new JPanel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        propValuesPanel = new JPanel();
        nameLbl = new JLabel();
        extensionLbl = new JLabel();
        sizeLbl = new JLabel();
        infoPanel = new JPanel();
        actionLbl = new JLabel();
        statusLbl = new JLabel();

        //======== this ========
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
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
            }
            menuBar1.add(optionsMenu);
        }
        setJMenuBar(menuBar1);

        //======== buttonsPanel ========
        {

            // JFormDesigner evaluation mark
            buttonsPanel.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), buttonsPanel.getBorder())); buttonsPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            buttonsPanel.setLayout(new BorderLayout());

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
            buttonsPanel.add(panel5, BorderLayout.WEST);
        }
        contentPane.add(buttonsPanel, BorderLayout.NORTH);

        //======== scrollPane1 ========
        {

            //---- fileTree ----
            fileTree.setMinimumSize(new Dimension(200, 0));
            fileTree.setBorder(new TitledBorder("File Tree"));
            fileTree.setPreferredSize(new Dimension(200, 72));
            fileTree.addTreeExpansionListener(new TreeExpansionListener() {
                @Override
                public void treeCollapsed(TreeExpansionEvent e) {}
                @Override
                public void treeExpanded(TreeExpansionEvent e) {
                    fileTreeTreeExpanded(e);
                }
            });
            fileTree.addTreeSelectionListener(e -> fileTreeValueChanged(e));
            scrollPane1.setViewportView(fileTree);
        }
        contentPane.add(scrollPane1, BorderLayout.CENTER);

        //======== propPanel ========
        {
            propPanel.setPreferredSize(new Dimension(200, 64));
            propPanel.setBorder(new TitledBorder("File Properties"));
            propPanel.setLayout(new BorderLayout());

            //======== propNamesPanel ========
            {
                propNamesPanel.setLayout(new BoxLayout(propNamesPanel, BoxLayout.Y_AXIS));

                //---- label8 ----
                label8.setText("Name");
                propNamesPanel.add(label8);

                //---- label9 ----
                label9.setText("Extension");
                propNamesPanel.add(label9);

                //---- label10 ----
                label10.setText("Size");
                propNamesPanel.add(label10);
            }
            propPanel.add(propNamesPanel, BorderLayout.WEST);

            //======== propValuesPanel ========
            {
                propValuesPanel.setLayout(new BoxLayout(propValuesPanel, BoxLayout.Y_AXIS));
                propValuesPanel.add(nameLbl);
                propValuesPanel.add(extensionLbl);
                propValuesPanel.add(sizeLbl);
            }
            propPanel.add(propValuesPanel, BorderLayout.EAST);
        }
        contentPane.add(propPanel, BorderLayout.EAST);

        //======== infoPanel ========
        {
            infoPanel.setLayout(new BorderLayout());
            infoPanel.add(actionLbl, BorderLayout.EAST);

            //---- statusLbl ----
            statusLbl.setText("Disconnected");
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
    private JPanel buttonsPanel;
    private JPanel panel5;
    private JButton connectBtn;
    private JButton loginBtn;
    private JScrollPane scrollPane1;
    private JTree fileTree;
    private JPanel propPanel;
    private JPanel propNamesPanel;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JPanel propValuesPanel;
    private JLabel nameLbl;
    private JLabel extensionLbl;
    private JLabel sizeLbl;
    private JPanel infoPanel;
    private JLabel actionLbl;
    private JLabel statusLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
