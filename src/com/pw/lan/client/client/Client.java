package com.pw.lan.client.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by aradej on 2016-05-13.
 */
public class Client implements Runnable {
    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    @SuppressWarnings("unused")
    private int id;

    public Client(String host, String port)
            throws Exception {
        socket = new Socket(host, Integer.parseInt(port));
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());

        new Thread(this).start();
        send("Hello");
    }

    public synchronized boolean isDisconnected() {
        return socket == null;
    }

    public void close() {
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
        } finally {
            output = null;
            input = null;
            socket = null;
        }
    }

    public Object receive() {
        try {
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //log
        }
        return null;
    }
    public void run() {
        while (handle(receive()));
        this.close();
    }

    public synchronized boolean handle(Object data){
        if (data == null) {
            return false;
        } else if (data instanceof String) {
            //doSomething
        }
        return true;
    }

    void send(Object obj) {
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
                output.writeObject(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
