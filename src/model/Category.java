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
public class Category {

    private String category;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * creates a new category
     */
    public void createCategory(){
        SQLConnection c = SQLConnection.getConnection();
        c.categoryCreate(category);
    }
    
    /**
     * changes the value of a category 
     * @param new_category the new value of the category
     */
    public void changeCategory(String new_category){
        SQLConnection c = SQLConnection.getConnection();
        c.categoryModify(category, new_category);
    }
    
    /**
     * deletes a category
     */
    public void deleteCategory(){
        SQLConnection c = SQLConnection.getConnection();
        c.categoryDelete(category);
    }
    
    private void print(String msg){
        System.out.println(msg);
    }
    
}
