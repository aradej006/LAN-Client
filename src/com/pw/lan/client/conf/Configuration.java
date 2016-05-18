package com.pw.lan.client.conf;

import com.pw.lan.client.client.NetworkInformation;

import java.io.*;

/**
 * Created by aradej on 2016-05-18.
 */
public class Configuration {

    private static final String CONFFILENAME = "conf.txt";
    private NetworkInformation networkInformation;

    public void readConfiguration(){
        try(BufferedReader br = new BufferedReader(new FileReader(CONFFILENAME))) {
            String line = br.readLine();
            networkInformation = new NetworkInformation();
            networkInformation.setIpAddress(line.split(" ")[1]);
            line = br.readLine();
            networkInformation.setPort(line.split(" ")[1]);
            line = br.readLine();
            networkInformation.setLogin(line.split(" ")[1]);
            line = br.readLine();
            networkInformation.setPassword(line.split(" ")[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfiguration(){
        File fout = new File(CONFFILENAME);
        BufferedWriter bw;
        try (FileOutputStream fos = new FileOutputStream(fout)) {
            bw = new BufferedWriter(new OutputStreamWriter(fos));for (int i = 0; i < 10; i++) {
                bw.write("network " + networkInformation.getIpAddress());
                bw.newLine();
                bw.write("port " + networkInformation.getPort());
                bw.newLine();
                bw.write("login " + networkInformation.getLogin());
                bw.newLine();
                bw.write("password " + networkInformation.getPassword());
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public NetworkInformation getNetworkInformation() {
        return networkInformation;
    }

    public void setNetworkInformation(NetworkInformation networkInformation) {
        this.networkInformation = networkInformation;
    }
}
