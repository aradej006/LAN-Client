package com.pw.lan.client;

import com.pw.lan.client.tftp.TFTPUtils;
import com.pw.lan.client.tftpclient.TFTPClient;

public class Main {

    public static void main(String[] args) {
        //Window w = new Window();

        String pathToFile = "";
        String pathToFileOnServer = "";

        TFTPClient tftpClient = new TFTPClient(10001,"192.168.1.8");
        tftpClient.sendFile(pathToFile + "999.txt","1234.txt");
        tftpClient.getFile("1234.txt", pathToFileOnServer + "999new.txt");

    }


}
