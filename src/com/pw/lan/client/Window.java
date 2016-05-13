package com.pw.lan.client;

import com.pw.lan.client.client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by aradej on 2016-05-13.
 */
public class Window extends JFrame{
    private JPanel rootPanel;
    private JTextField ipAddress;
    private JTextField port;
    private JButton connect;
    private Client client;

    public Window(){
        super("LAN Client App");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(connect.getText().equals("Connect")){
                    //connect
                    try {
                        client = new Client(ipAddress.getText(), port.getText());
                        connect.setText("Disconnect");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else{
                    //disconnect
                    client.forceLogout();
                    connect.setText("Connect");
                }
            }
        });
    }
}
