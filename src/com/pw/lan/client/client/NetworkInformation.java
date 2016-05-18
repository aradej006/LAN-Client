package com.pw.lan.client.client;

/**
 * Created by aradej on 2016-05-18.
 */
public class NetworkInformation {

    private String ipAddress;
    private String port;
    private String login;
    private String password;

    public NetworkInformation() {
    }

    public NetworkInformation(String ipAddress, String port, String login, String password) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public boolean isReady() {
        if (ipAddress == null) return false;
        if (port == null) return false;
        if (login == null) return false;
        if (password == null) return false;
        return true;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
