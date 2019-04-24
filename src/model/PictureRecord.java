/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Shakhaout Rahman
 */
public class PictureRecord implements Comparable<PictureRecord>{
    private int picID;
    private String path;
    private byte[] preview;

    public PictureRecord(int picID, String path) {
        this.picID = picID;
        this.path = path;
        this.preview = null;
    }

    public PictureRecord(int picID, String path, byte[] preview) {
        this.picID = picID;
        this.path = path;
        this.preview = preview;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }
    
    @Override
    public int compareTo(PictureRecord o) {
        if(this.getPath().compareTo(o.getPath()) < 0){
            return -1;
        } else if (this.getPath().compareTo(o.getPath()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
