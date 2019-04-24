/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Shakhaout Rahman
 */
public class Annotation {
    
    private int annID;
    private int picID;
    private String text;
    double x;
    double y;
    int size;
    String color;
    private Timestamp added;

    public Annotation(int annID, int picID, String text, double x, double y, int size, String color, Timestamp added) {
        this.annID = annID;
        this.picID = picID;
        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.added = added;
    }
    
    public int getAnnID() {
        return annID;
    }

    public void setAnnID(int annID) {
        this.annID = annID;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    
    
}
