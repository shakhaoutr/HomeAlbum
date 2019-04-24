/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import homealbum.SQLConnection;


/**
 *
 * @author Shakhaout Rahman
 */
public class Label {

    private String label;

    public Label(String category) {
        this.label = category;
    }

    public String getCategory() {
        return label;
    }

    public void setCategory(String category) {
        this.label = category;
    }
    
    /**
     * creates a new label
     */
    public void createLabel(){
        SQLConnection c = SQLConnection.getConnection();
        c.labelCreate(label);
    }
    
    /**
     * changes the value of a label 
     * @param new_label the new value of the label
     */
    public void changeLabel(String new_label){
        SQLConnection c = SQLConnection.getConnection();
        c.labelModify(label, new_label);
    }
    
    /**
     * deletes a label
     */
    public void deleteLabel(){
        SQLConnection c = SQLConnection.getConnection();
        c.labelDelete(label);
    }
    
    private void print(String msg){
        System.out.println(msg);
    }
    
}
