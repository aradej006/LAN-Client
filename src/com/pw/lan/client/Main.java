package com.pw.lan.client;

import com.pw.lan.client.conf.Configuration;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
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
        SwingUtilities.invokeLater(() -> new MainWindow() );
    }
}
