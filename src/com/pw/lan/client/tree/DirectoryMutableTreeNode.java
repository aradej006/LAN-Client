package com.pw.lan.client.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by aradej on 2016-05-18.
 */
public class DirectoryMutableTreeNode extends DefaultMutableTreeNode{

    private boolean canRead;
    private boolean canWrite;

    public DirectoryMutableTreeNode(Object userObject, boolean canRead, boolean canWrite) {
        super(userObject);
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public String getPermissions() {
        if (canWrite) return "RW";
        else return "R-";
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }
}
