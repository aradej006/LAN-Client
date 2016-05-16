package com.pw.lan.client.client;

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

    public Client(String host, String port) throws Exception {
        name = InetAddress.getLocalHost().getHostName();
        LOGGER.log(Level.INFO, "Client: Creating client {0}", name);
        socket = new Socket(host, Integer.parseInt(port));
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
    }

    private synchronized boolean handle(Object data){
        if (data == null) {
            return false;
        } else{
            LOGGER.log(Level.INFO, "Client: Received data : {0}", data.toString());
            if (data instanceof Map) {
                Map msgs = (Map) data;
                if(msgs.get(Msg.TYPE).toString().equals(Msg.DO_LOGIN)){
                }else if(msgs.get(Msg.TYPE).toString().equals(Msg.LOGINRESULT)){
                    loggedIn = true;
                    this.notify();
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
        if (socket != null)
            try {
                LOGGER.log(Level.INFO, "Client: {0} sending logout.", name);
                output.writeObject(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public boolean login(String login, String password){
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

}
