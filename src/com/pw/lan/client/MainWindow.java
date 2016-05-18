/*
 * Created by JFormDesigner on Mon May 16 13:56:37 CEST 2016
 */

package com.pw.lan.client;

import javax.swing.border.*;
import javax.swing.event.*;

import com.pw.lan.client.client.Client;
import com.pw.lan.client.client.NetworkInformation;
import com.pw.lan.client.conf.Configuration;
import com.pw.lan.client.tftpclient.TFTPClient;
import com.pw.lan.client.tree.DirectoryMutableTreeNode;
import com.pw.lan.client.tree.FileMutableTreeNode;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
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

    private NetworkInformation networkInformation;
    private Client client;
    private NetworkWindow networkWindow;
    private boolean expandingFilesTree;
    private boolean networkWindowOpened;
    private Configuration conf;

    public MainWindow() {
        super("LAN Client App");
        initComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        expandingFilesTree = false;
        networkWindowOpened = false;

        resetTree();
        downloadBtn.setEnabled(false);
        uploadBtn.setEnabled(false);
        connectBtn.setEnabled(false);

        conf = new Configuration();
        conf.readConfiguration();
        setNetworkInformation(conf.getNetworkInformation());
    }

    private void resetTree(){
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        root.setUserObject("root");
        model.reload(root);
        fileTree.setEnabled(false);
    }
    private void networkMenuActionPerformed(ActionEvent e) {
        networkWindowOpened = true;
        this.setEnabled(false);
        this.setFocusable(false);
        networkWindow = new NetworkWindow(this);
    }

    public void setNetworkWindowOpened(boolean networkWindowOpened) {
        this.networkWindowOpened = networkWindowOpened;
    }

    public NetworkInformation getNetworkInformation() {
        return networkInformation;
    }

    public void setNetworkInformation(NetworkInformation networkInformation) {
        this.networkInformation = networkInformation;
        if (networkInformation.isReady()) {
            connectBtn.setEnabled(true);
        }
    }

    private void connectBtnActionPerformed(ActionEvent e) {
        if (connectBtn.getText().equals("Connect")) {
            try {
                connectBtn.setText("Connecting...");
                client = new Client(networkInformation.getIpAddress(), networkInformation.getPort());
                client.setMainWindow(this);
                connectBtn.setText("Disconnect");
                loginBtn.setEnabled(true);
                statusLbl.setText("Connected, Not Logged In");
            } catch (Exception e1) {
                JOptionPane.showConfirmDialog(this, e1.getMessage(), "Connection Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                connectBtn.setText("Connect");
                e1.printStackTrace();
            }
        } else {
            client.forceLogout();
            client = null;
            connectBtn.setText("Connect");
            statusLbl.setText("Disconnected");
            loginBtn.setEnabled(false);
            resetTree();
        }
    }

    private void loginBtnActionPerformed(ActionEvent e) {
        statusLbl.setText("Connected, Logging In...");
        if (client.login(networkInformation.getLogin(), networkInformation.getPassword())) {
            loginBtn.setEnabled(false);
            statusLbl.setText("Connected, Log In");
        } else {
            statusLbl.setText("Connected, Not Logged In");
            JOptionPane.showConfirmDialog(this, "Incorrect Login or Password!", "Error! Incorrect login data!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateFilesTree(String filesPath, Map<String, String> files) {
        String[] split = filesPath.split("/");
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node = root;
        root.setUserObject("root");
        if (split.length == 1) root.removeAllChildren();
        for (int i = 1; i < split.length; i++) {
            Enumeration children = node.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode n = (DefaultMutableTreeNode) children.nextElement();
                if (n.getUserObject().equals(split[i])) {
                    node = n;
                    if (i == split.length - 1) {
                        node.removeAllChildren();
                    }
                    break;
                }
            }
        }
        for (String key : files.keySet()) {
            DefaultMutableTreeNode n;
            String[] splitValue = files.get(key).split(" ");
            if (splitValue[0].equals("dir")) {
                n = new DirectoryMutableTreeNode(key, Boolean.parseBoolean(splitValue[1]), Boolean.parseBoolean(splitValue[2]));
                n.add(new DefaultMutableTreeNode("<items>"));
            } else {
                n = new FileMutableTreeNode(key, splitValue[0], Long.parseLong(splitValue[1]));
            }
            node.add(n);
        }
        model.reload(root);
        fileTree.expandPath(new TreePath(node.getPath()));
        expandingFilesTree = false;
        actionLbl.setText("Fetched");
        fileTree.setEnabled(true);
    }

    private void fileTreeTreeExpanded(TreeExpansionEvent e) {
        if (!expandingFilesTree) {
            String filesPath = e.getPath().toString().substring(1, e.getPath().toString().length() - 1).replace(", ", "/") + "/";
            client.getFiles(filesPath);
            expandingFilesTree = true;
            actionLbl.setText("Fetching...");
        }
    }

    private void fileTreeValueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        if (n instanceof FileMutableTreeNode) {
            FileMutableTreeNode node = (FileMutableTreeNode) n;
            nameLbl.setText(node.getName());
            extensionLbl.setText(node.getExtension());
            sizeLbl.setText(node.getSize());
            directoryLbl.setText("");
            permissionLbl.setText("");
            downloadBtn.setEnabled(true);
            uploadBtn.setEnabled(false);
        } else if (n instanceof DirectoryMutableTreeNode) {
            DirectoryMutableTreeNode node = (DirectoryMutableTreeNode) n;
            directoryLbl.setText(node.getUserObject().toString());
            permissionLbl.setText(node.getPermissions());
            nameLbl.setText("");
            extensionLbl.setText("");
            sizeLbl.setText("");
            downloadBtn.setEnabled(false);
            if (node.isCanWrite()) {
                uploadBtn.setEnabled(true);
            } else {
                uploadBtn.setEnabled(false);
            }
        } else {
            directoryLbl.setText(n.getUserObject().toString());
            permissionLbl.setText("RW");
            nameLbl.setText("");
            extensionLbl.setText("");
            sizeLbl.setText("");
            uploadBtn.setEnabled(false);
        }
        String filesPath = e.getPath().toString().substring(1, e.getPath().toString().length() - 1).replace(", ", "/");
        currentPathLbl.setText(filesPath);

    }

    private void thisWindowClosing(WindowEvent e) {
        if (client != null)
            client.forceLogout();
        conf.setNetworkInformation(networkInformation);
        conf.saveConfiguration();
    }

    private void thisWindowGainedFocus(WindowEvent e) {
    }

    private void thisWindowActivated(WindowEvent e) {
    }

    private void downloadBtnActionPerformed(ActionEvent e) throws IOException {
        //TODO add your code to download file

        String directory =  System.getProperty("user.dir");
        String pathToFile = currentPathLbl.getText();
        TFTPClient tftpClient = new TFTPClient(20000,networkInformation.getIpAddress());
        tftpClient.getFile(pathToFile, directory + "/" + pathToFile);
    }

    private void uploadBtnActionPerformed(ActionEvent e) {
        // TODO add your code to upload file

        String pathToFile = currentPathLbl.getText();
        String directory =  System.getProperty("user.dir");
        TFTPClient tftpClient = new TFTPClient(20000,networkInformation.getIpAddress());
        tftpClient.sendFile(directory + "/" + pathToFile, pathToFile);

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Adrian Radej
        menuBar1 = new JMenuBar();
        optionsMenu = new JMenu();
        networkMenu = new JMenuItem();
        buttonsPanel = new JPanel();
        panel5 = new JPanel();
        currentPathPane = new JPanel();
        currentPathDescLbl = new JLabel();
        currentPathLbl = new JLabel();
        buttonsWrapperPane = new JPanel();
        buttonsPane = new JPanel();
        connectBtn = new JButton();
        loginBtn = new JButton();
        propPanel = new JPanel();
        tftpActionPane = new JPanel();
        panel2 = new JPanel();
        uploadBtn = new JButton();
        downloadBtn = new JButton();
        directoryPropPane = new JPanel();
        directoryPropsActionPanel = new JPanel();
        directoryDescLbl = new JLabel();
        permissionDescLbl = new JLabel();
        directoryPropsValuePanel = new JPanel();
        directoryLbl = new JLabel();
        permissionLbl = new JLabel();
        propFilePane = new JPanel();
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
        treePane = new JPanel();
        scrollPane1 = new JScrollPane();
        fileTree = new JTree();

        //======== this ========
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                thisWindowActivated(e);
            }
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                thisWindowGainedFocus(e);
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

                //======== currentPathPane ========
                {
                    currentPathPane.setLayout(new BorderLayout());

                    //---- currentPathDescLbl ----
                    currentPathDescLbl.setText("Current Path: ");
                    currentPathPane.add(currentPathDescLbl, BorderLayout.WEST);

                    //---- currentPathLbl ----
                    currentPathLbl.setText("root/");
                    currentPathPane.add(currentPathLbl, BorderLayout.EAST);
                }
                panel5.add(currentPathPane, BorderLayout.SOUTH);

                //======== buttonsWrapperPane ========
                {
                    buttonsWrapperPane.setLayout(new BorderLayout());

                    //======== buttonsPane ========
                    {
                        buttonsPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                        buttonsPane.setLayout(new FlowLayout());

                        //---- connectBtn ----
                        connectBtn.setText("Connect");
                        connectBtn.addActionListener(e -> connectBtnActionPerformed(e));
                        buttonsPane.add(connectBtn);

                        //---- loginBtn ----
                        loginBtn.setText("Login");
                        loginBtn.setEnabled(false);
                        loginBtn.addActionListener(e -> loginBtnActionPerformed(e));
                        buttonsPane.add(loginBtn);
                    }
                    buttonsWrapperPane.add(buttonsPane, BorderLayout.WEST);
                }
                panel5.add(buttonsWrapperPane, BorderLayout.CENTER);
            }
            buttonsPanel.add(panel5, BorderLayout.CENTER);
        }
        contentPane.add(buttonsPanel, BorderLayout.PAGE_START);

        //======== propPanel ========
        {
            propPanel.setPreferredSize(new Dimension(200, 64));
            propPanel.setBorder(new TitledBorder("Properties"));
            propPanel.setLayout(new BorderLayout());

            //======== tftpActionPane ========
            {
                tftpActionPane.setLayout(new BorderLayout());

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //---- uploadBtn ----
                    uploadBtn.setText("Upload");
                    uploadBtn.addActionListener(e -> uploadBtnActionPerformed(e));
                    panel2.add(uploadBtn, BorderLayout.WEST);

                    //---- downloadBtn ----
                    downloadBtn.setText("Download");
                    downloadBtn.addActionListener(e -> {
                        try {
                            downloadBtnActionPerformed(e);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
                    panel2.add(downloadBtn, BorderLayout.EAST);
                }
                tftpActionPane.add(panel2, BorderLayout.EAST);
            }
            propPanel.add(tftpActionPane, BorderLayout.SOUTH);

            //======== directoryPropPane ========
            {
                directoryPropPane.setBorder(new TitledBorder("Directory"));
                directoryPropPane.setLayout(new BorderLayout());

                //======== directoryPropsActionPanel ========
                {
                    directoryPropsActionPanel.setLayout(new BoxLayout(directoryPropsActionPanel, BoxLayout.Y_AXIS));

                    //---- directoryDescLbl ----
                    directoryDescLbl.setText("Directory");
                    directoryPropsActionPanel.add(directoryDescLbl);

                    //---- permissionDescLbl ----
                    permissionDescLbl.setText("Permission");
                    directoryPropsActionPanel.add(permissionDescLbl);
                }
                directoryPropPane.add(directoryPropsActionPanel, BorderLayout.WEST);

                //======== directoryPropsValuePanel ========
                {
                    directoryPropsValuePanel.setLayout(new BoxLayout(directoryPropsValuePanel, BoxLayout.Y_AXIS));
                    directoryPropsValuePanel.add(directoryLbl);
                    directoryPropsValuePanel.add(permissionLbl);
                }
                directoryPropPane.add(directoryPropsValuePanel, BorderLayout.EAST);
            }
            propPanel.add(directoryPropPane, BorderLayout.NORTH);

            //======== propFilePane ========
            {
                propFilePane.setBorder(new TitledBorder("File"));
                propFilePane.setLayout(new BorderLayout());

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
                propFilePane.add(propNamesPanel, BorderLayout.WEST);

                //======== propValuesPanel ========
                {
                    propValuesPanel.setLayout(new BoxLayout(propValuesPanel, BoxLayout.Y_AXIS));
                    propValuesPanel.add(nameLbl);
                    propValuesPanel.add(extensionLbl);
                    propValuesPanel.add(sizeLbl);
                }
                propFilePane.add(propValuesPanel, BorderLayout.EAST);
            }
            propPanel.add(propFilePane, BorderLayout.CENTER);
        }
        contentPane.add(propPanel, BorderLayout.LINE_END);

        //======== infoPanel ========
        {
            infoPanel.setLayout(new BorderLayout());
            infoPanel.add(actionLbl, BorderLayout.EAST);

            //---- statusLbl ----
            statusLbl.setText("Disconnected");
            infoPanel.add(statusLbl, BorderLayout.WEST);
        }
        contentPane.add(infoPanel, BorderLayout.PAGE_END);

        //======== treePane ========
        {
            treePane.setBorder(new TitledBorder("Files Tree"));
            treePane.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {
                scrollPane1.setBorder(null);

                //---- fileTree ----
                fileTree.setMinimumSize(new Dimension(200, 0));
                fileTree.setBorder(null);
                fileTree.setPreferredSize(new Dimension(200, 72));
                fileTree.setForeground(new Color(187, 187, 187));
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
            treePane.add(scrollPane1, BorderLayout.CENTER);
        }
        contentPane.add(treePane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Adrian Radej
    private JMenuBar menuBar1;
    private JMenu optionsMenu;
    private JMenuItem networkMenu;
    private JPanel buttonsPanel;
    private JPanel panel5;
    private JPanel currentPathPane;
    private JLabel currentPathDescLbl;
    private JLabel currentPathLbl;
    private JPanel buttonsWrapperPane;
    private JPanel buttonsPane;
    private JButton connectBtn;
    private JButton loginBtn;
    private JPanel propPanel;
    private JPanel tftpActionPane;
    private JPanel panel2;
    private JButton uploadBtn;
    private JButton downloadBtn;
    private JPanel directoryPropPane;
    private JPanel directoryPropsActionPanel;
    private JLabel directoryDescLbl;
    private JLabel permissionDescLbl;
    private JPanel directoryPropsValuePanel;
    private JLabel directoryLbl;
    private JLabel permissionLbl;
    private JPanel propFilePane;
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
    private JPanel treePane;
    private JScrollPane scrollPane1;
    private JTree fileTree;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
