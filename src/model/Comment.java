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
public class Comment {
    
    private int commID;
    private int picID;
    private String author;
    private String text;
    private Timestamp added;

    public Comment(int commID, int picID, String author, String text, Timestamp added) {
        this.commID = commID;
        this.picID = picID;
        this.author = author;
        this.text = text;
        this.added = added;
    }

    public int getCommID() {
        return commID;
    }

    public void setCommID(int commID) {
        this.commID = commID;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    @Override
    public String toString() {
        return "Comment{" + "commID=" + commID + ", picID=" + picID + ", author=" + author + ", text=" + text + ", added=" + added + '}';
    }
    
    
    
}
