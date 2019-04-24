/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Shakhaout Rahman
 */
public class Picture implements Comparable<Picture>{
    
    private int id;
    private String name;
    private String place;
    private java.sql.Date date;
    private int size;
    private Category category;
    private java.sql.Timestamp inserted;
    private java.sql.Timestamp lastModified;
    private String path;
    private byte[] img;
    private ArrayList<String> labels;


    public Picture(int id, String name, String place, Date date, int size, 
            Timestamp inserted, Timestamp lastModified, String path, byte[] img) {
        
        this(id, name, place, date, size, new Category("None"), inserted, lastModified, path, img, new ArrayList(0));
    }
    
    public Picture(int id, String name, String place, Date date, int size, 
            Timestamp inserted, Timestamp lastModified, String path, byte[] img, ArrayList<String> labels) {
        
        this(id, name, place, date, size, new Category("None"), inserted, lastModified, path, img, labels);
    }
    
    public Picture(int id, String name, String place, java.sql.Date date, int size, 
            String category, Timestamp inserted, Timestamp lastModified, String path, byte[] img) {
        
        this(id, name, place, date, size, new Category(category), inserted, lastModified, path, img, new ArrayList(0));
    }
    
    public Picture(int id, String name, String place, java.sql.Date date, int size, 
            String category, Timestamp inserted, Timestamp lastModified, String path, byte[] img, ArrayList<String> labels) {
        
        this(id, name, place, date, size, new Category(category), inserted, lastModified, path, img, labels);
    }
    
    public Picture(int id, String name, String place, java.sql.Date date, int size, 
            Category category, Timestamp inserted, Timestamp lastModified, String path, byte[] img) {
        
        this(id, name, place, date, size, category, inserted, lastModified, path, img, new ArrayList(0));
    }
    
    public Picture(int id, String name, String place, java.sql.Date date, int size, 
            Category category, Timestamp inserted, Timestamp lastModified, String path, byte[] img, ArrayList<String> labels) {
        
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
        this.size = size;
        this.category = category;
        this.inserted = inserted;
        this.lastModified = lastModified;
        this.path = path;
        this.img = img;
        this.labels = labels;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public java.sql.Timestamp getInserted() {
        return inserted;
    }

    public void setInserted(java.sql.Timestamp inserted) {
        this.inserted = inserted;
    }

    public java.sql.Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(java.sql.Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    @Override
    public int compareTo(Picture o) {
        if(this.getName().compareTo(o.getName()) < 0){
            return -1;
        } else if (this.getName().compareTo(o.getName()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
