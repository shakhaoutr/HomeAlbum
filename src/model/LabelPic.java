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
public class LabelPic {
    int lpicID;
    String label;
    int picture;

    public LabelPic(int lpicID, String label, int picture) {
        this.lpicID = lpicID;
        this.label = label;
        this.picture = picture;
    }

    public int getLpicID() {
        return lpicID;
    }

    public void setLpicID(int lpicID) {
        this.lpicID = lpicID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "LabelPic{" + "lpicID=" + lpicID + ", label=" + label + ", picture=" + picture + '}';
    }
    
    
}
