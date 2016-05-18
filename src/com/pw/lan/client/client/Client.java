package com.pw.lan.client.client;

import com.pw.lan.client.MainWindow;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aradej on 2016-05-13.
 */
public class Client implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;
    private boolean loggedIn = false;
    private MainWindow mainWindow;

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Client(String host, String port) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        name = InetAddress.getLocalHost().getHostName();
        LOGGER.log(Level.INFO, "Client: Creating client {0}", name);
//        socket = new Socket(host, Integer.parseInt(port));
        socket = SSLSocketFactory.getDefault().createSocket(host,Integer.parseInt(port));
        LOGGER.log(Level.INFO, "Client: {0} getting streams...", name);
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
        LOGGER.log(Level.INFO, "Client: {0} got streams.", name);
        new Thread(this).start();
        Map<String,String> map = new HashMap<>();
        map.put(Msg.TYPE,Msg.HELLO);
        map.put(Msg.NAME, name);
        send(map);
    }

    public synchronized boolean isDisconnected() {
        return socket == null;
    }

    private void close() {
        try {
            output.close();
            input.close();
            socket.close();
            LOGGER.log(Level.INFO, "Client: Closed all sockets properly.");
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Client: Error closing client {0} ", name);
        } finally {
            output = null;
            input = null;
            socket = null;
        }
    }

    private Object receive() {
        try {
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //log
        }
        return null;
    }

    public void run() {
        while (handle(receive()));
        LOGGER.log(Level.INFO, "Client: Received logout.", name);
        this.close();
        LOGGER.log(Level.INFO, "Client: Thread end.", name);
    }

    private synchronized boolean handle(Object data){
        if (data == null) {
            return false;
        } else{
            LOGGER.log(Level.INFO, "Client: Received data : {0}", data.toString());
            if (data instanceof Map) {
                Map msgs = (Map) data;
                if(msgs.get(Msg.TYPE).toString().equals(Msg.DO_LOGIN)){
                    //do something
                }else if(msgs.get(Msg.TYPE).toString().equals(Msg.LOGINRESULT)){
                    if(msgs.get(Msg.LOGINMSG).toString().equals(Msg.LOGINCONFIRMED)){
                        loggedIn = true;
                        this.notify();
                        msgs = new HashMap<>();
                        msgs.put(Msg.TYPE, Msg.GETFILES);
                        send(msgs);
                    } else {
                        loggedIn = false;
                        this.notify();
                    }

                }else if(msgs.get(Msg.TYPE).toString().equals(Msg.FILES)){
                    mainWindow.updateFilesTree(msgs.get(Msg.FILESPATH).toString(),(Map<String,String>)msgs.get(Msg.FILEMAP));
                }
            }else{
                LOGGER.log(Level.INFO, "Client: Received bad data.");
            }
        }
        return true;
    }

    private void send(Object obj) {
        if (output != null)
            try {
                output.writeObject(obj);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public synchronized void forceLogout() {
        if (socket != null) {
            try {
                LOGGER.log(Level.INFO, "Client: {0} sending logout.", name);
                output.writeObject(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean login(String login, String password){
        Map msgs = new HashMap<String, String>();
        msgs.put(Msg.TYPE,Msg.LOGIN);
        msgs.put(Msg.LOGIN,login);
        msgs.put(Msg.PASSWORD,password);
        msgs.put(Msg.ALGORITHM,"SHA-256");
        send(msgs);
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loggedIn;
    }

    public void getFiles(String filesPath){
        Map<String,String> msgs = new HashMap<>();
        msgs.put(Msg.TYPE, Msg.GETFILES);
        msgs.put(Msg.FILESPATH,filesPath);
        send(msgs);
    }
}
