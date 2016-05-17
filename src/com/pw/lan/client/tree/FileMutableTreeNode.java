package com.pw.lan.client.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by arade on 17-May-16.
 */
public class FileMutableTreeNode extends DefaultMutableTreeNode {

    private String extension;
    private Long size;

    public FileMutableTreeNode(Object userObject, String extension, Long size) {
        super(userObject);
        this.extension = extension;
        this.size = size;
    }

    public String getName() {
        return userObject.toString().substring(0,userObject.toString().lastIndexOf('.'));
    }

    public String getExtension() {
        return extension;
    }

    public String getSize() {
        return humanReadableByteCount(size);
    }

    private static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("kMGTPE").charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
