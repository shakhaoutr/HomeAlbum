/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homealbum;

import model.*;
import view.gui.AlertWindow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 *  offers connector to the SQL Database
 * 
 * 
 * @author Shakhaout Rahman
 */
public class SQLConnection {
    
    private static SQLConnection single_instance = null;
    private Connection c = null;
    private Statement s;

    /**
     * constructor for the connector
     */
    private SQLConnection() {
        Settings s = Settings.getSettings();
        try {
            Class.forName(s.DB_CLASS);  // retrieves the libraries for the connector
            this.c = DriverManager.getConnection(s.DB_DRIVER+"//"+s.DB_HOST+"/"+
                    s.DB_NAME+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
                    s.DB_USER, s.DB_PASSWD);
            
            createTables();
        } catch (ClassNotFoundException ex) {
            showError(ex, "SQL Class Driver Not Found when connecting to database");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when connecting to database");
        }
    }
    
    /**
     * returns instance of the class
     * @return  the instance of the class
     */
    public static SQLConnection getConnection(){
        if( single_instance == null){
            single_instance = new SQLConnection();
        }
        
        return single_instance;
    }
    
/* ******************* CATEGORY CREATION AND MODIFICATION ******************* */ 
    
    /**
     * creates a new category
     * 
     * @param category      the name of the category
     */
    public void categoryCreate(String category){
        String sql = "INSERT INTO category (category) VALUES (?)";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, category);
            p.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when creating category");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating category");
        }
    }
    
    /**
     * changes the value of a category
     * 
     * @param old_category      the name of the category to change
     * @param new_category      the new value of the category
     */
    public void categoryModify(String old_category, String new_category){
        String query = "UPDATE category SET category = ? WHERE category = ? ";
        try {
            PreparedStatement p = c.prepareStatement(query);
            
            p.setString(1, new_category);
            p.setString(2, old_category);
            
            p.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when editing category");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing category");
        }
    }
    
    /**
     * deletes a category
     * 
     * @param category      the name of the category to delete
     */
    public void categoryDelete(String category){
        String query = "DELETE FROM category WHERE category = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(query);
            
            p.setString(1, category);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting category");
        }
    }
    
    /**
     * return the category if it exist
     * 
     * @param category      the id of the category to look for
     * @return              the string of the category or an empty string
     */
    public String categorySelect(String category){
        String sql = "SELECT category FROM category WHERE category = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, category);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                return rs.getString("category");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a category");
            return "";
        }
    }
    
    /**
     * returns a string list of all categories
     * @return      the list of categories or an empty list
     */
    public ArrayList<String> categorySelectAll(){
        String sql = "SELECT category FROM category";
        ArrayList<String> cats = new ArrayList<>(10);
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    cats.add(rs.getString("category"));
                }
            }
            cats.trimToSize();
            return cats;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading all categories");
            cats.trimToSize();
            return cats;
        }
    }
    
    
    
/* ******************* END CATEGORY CREATION AND MODIFICATION ******************* */
    
    
/* ******************* LABEL CREATION AND MODIFICATION ******************* */ 
    
    /**
     * creates a new label
     * 
     * @param label         the name of the label
     */
    public void labelCreate(String label){
        String sql = "INSERT INTO label(label) VALUES (?)";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            p.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when creating label");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating label");
        }
    }
    
    /**
     * changes the value of a label 
     * 
     * @param old_label     the name of the label to change
     * @param new_label     the new value of the label
     */
    public void labelModify(String old_label, String new_label){
        String query = "UPDATE label SET label = ? WHERE label = ? ";
        try {
            PreparedStatement p = c.prepareStatement(query);
            
            p.setString(1, new_label);
            p.setString(2, old_label);
            
            p.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when editing label");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing label");
        }
    }
    
    /**
     * deletes a label
     * 
     * @param label the name of the label to delete
     */
    public void labelDelete(String label){
        String query = "DELETE FROM label WHERE label = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(query);
            
            p.setString(1, label);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting label");
        }
    }
    
    /**
     * return the label if it exist
     * 
     * @param label     the id of the label to look for
     * @return          the string label or an empty string
     */
    public String labelSelect(String label){
        String sql = "SELECT label FROM label WHERE label = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                return rs.getString("label");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when retrieving a label");
            return "";
        }
    }
    
    /**
     * return the list of all labels
     * @return      the list of labels or an empty list
     */
    public ArrayList<String> labelSelectAll(){
        String sql = "SELECT label FROM label";
        ArrayList<String> labs = new ArrayList<>(10);
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    labs.add(rs.getString("label"));
                }
            }
            labs.trimToSize();
            return labs;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when retrieving all labels");
            labs.trimToSize();
            return labs;
        }
    }
    
    
    
/* ******************* END CATEGORY CREATION AND MODIFICATION ******************* */
    
    
/* ******************* PICTURE CREATION AND MODIFICATION ******************* */ 
    
    /**
     * inserts a new picture to the database
     * 
     * @param name      name of the picture
     * @param date      the day the picture was taken
     * @param place     the place where the picture was taken
     * @param size      the size of the picture in Kb
     * @param path      the directory of the picture
     * @return          true if insertion was successful else false
     */
    public boolean pictureInsert(String name, java.sql.Date date, String place, int size, String path, byte[] preview){
        String sql = "INSERT INTO picture (name, date_taken, place_taken, size, path, preview)"
                + "VALUES (?,?,?,?,?,?)";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);           
            
            p.setString(1, name);
            p.setDate(2, date);
            p.setString(3, place);
            p.setInt(4, size);
            p.setString(5, path);
            p.setBytes(6, preview);
            
            p.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when inserting a picture");
            return false;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when inserting a picture");
            return false;
        }
    }
    
    /**
     * inserts a new picture to the database
     * 
     * @param name      name of the picture
     * @param date      the day the picture was taken
     * @param place     the place where the picture was taken
     * @param size      the size of the picture in Kb
     * @param category  the category of the picture
     * @param path      the directory of the picture
     * @return          true if insertion was successful
     */
    public boolean pictureInsert(String name, java.sql.Date date, String place, int size, String category, String path, byte[] preview){
        String sql = "INSERT INTO picture (name, date_taken, place_taken, size, category, path, preview)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, name);
            p.setDate(2, date);
            p.setString(3, place);
            p.setInt(4, size);
            p.setString(5, category);
            p.setString(6, path);
            p.setBytes(7, preview);
            
            p.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when inserting a picture");
            return false;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when inserting a picture");
            return false;
        }
    }
    
/* ************************* MODIFY PICTURE INFORMATION ************************* */
    
    /**
     * modifies the information of a picture
     * 
     * @param id        the picture to modify
     * @param name      the new name
     * @param date      the new date
     * @param place     the new place
     * @param size      the new size
     * @param category  the new category
     * @param path      the new path
     * @return          true if modification was successful
     */
    public boolean pictureModify(int id, String name, java.sql.Date date, String place, int size, String category, String path, byte[] preview){
        if(pictureNameModify(id, name)){
            pictureDateModify(id, date);
            picturePlaceModify(id, place);
            pictureSizeModify(id, size);
            pictureCategoryModify(id, category);
            picturePathModify(id, path);
            picturePreviewModify(id, preview);
            return true;
        }
        return false;
    }
    
    /**
     * modifies the name of the picture
     * 
     * @param id    the picture to modify
     * @param name  the new name of the picture 
     * @return      true if modification was successful
     */
    public boolean pictureNameModify(int id, String name){
        String sql = "UPDATE picture SET name = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, name);
            p.setInt(2, id);
            
            p.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError(ex, "SQL Integirty Error when editing a picture");
            return false;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
            return false;
        }
    }
    
    /** 
     * modifies the date of when the picture was taken 
     * 
     * @param id    the picture to modify
     * @param date  the new date
     */
    public void pictureDateModify(int id, java.sql.Date date){
        String sql = "UPDATE picture SET date_taken = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setDate(1, date);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    /**
     * modifies the place where the picture was taken 
     * 
     * @param id    the picture to modify
     * @param place the new place
     */
    public void picturePlaceModify(int id, String place){
        String sql = "UPDATE picture SET place_taken = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, place);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    /**
     * modifies the category of the picture 
     * 
     * @param id    the picture to modify
     * @param size  the new size
     */
    public void pictureSizeModify(int id, int size){
        String sql = "UPDATE picture SET size = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, size);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    /**
     * modifies the category of the picture 
     * 
     * @param id        the picture to modify
     * @param category  the new category
     */
    public void pictureCategoryModify(int id, String category){
        String sql = "UPDATE picture SET category = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, category);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    /**
     * modifies the directory of the picture 
     * 
     * @param id    the picture to modify
     * @param path  the new path
     */
    public void picturePathModify(int id, String path){
        String sql = "UPDATE picture SET path = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, path);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    /**
     * modifies the directory of the picture 
     * 
     * @param id        the picture to modify
     * @param preview   the new preview
     */
    public void picturePreviewModify(int id, byte[] preview){
        String sql = "UPDATE picture SET preview = ? WHERE picID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setBytes(1, preview);
            p.setInt(2, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing a picture");
        }
    }
    
    
    
    /**
     * delete a picture
     * @param id    the id of the picture to delete
     */
    public void pictureDelete(int id){
        String sql = "DELETE FROM picture WHERE picID = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, id);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting a picture");
        }
    }
    
    /**
     * gets the name of a picture
     * 
     * @param id    the id of the picture
     * @return      the picture name, an empty string if not found
     */
    public String getPictureName(int id){
        String sql = "SELECT name "
                + "FROM picture WHERE picID = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, id);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return "";
        }
    }
    
    /**
     * gets the path of a picture
     * 
     * @param id    the id of the picture
     * @return      the picture path, an empty string if not found
     */
    public String getPicturePath(int id){
        String sql = "SELECT path "
                + "FROM picture WHERE picID = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, id);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                return rs.getString("path");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return "";
        }
    }
    
    /**
     * gets the id of a picture
     * 
     * @param path  the path of the picture
     * @return      the picture id, "-1" if not found
     */
    public int getPictureId(String path){
        String sql = "SELECT picID "
                + "FROM picture WHERE path = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, path);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                return rs.getInt("picID");
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return -1;
        }
    }
    
    /**
     * gets the paths of all pictures
     * 
     * @return      an ArrayList-String- of id and paths, or an empty list or null
     */
    public ArrayList<PictureRecord> getAllPictureRecords(){
        String sql = "SELECT picID, path, preview "
                + "FROM picture";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> recs = new ArrayList<>(10);
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    recs.add(new PictureRecord(id, path, preview));
                }
                recs.trimToSize();
                return recs;
            } else {
                recs.trimToSize();
                return recs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }
    
    /**
     * gets the paths of all pictures by category
     * 
     * @param cat   the category of the pictures
     * @return      an ArrayList-String- of id and paths, or an empty list or null
     */
    public ArrayList<PictureRecord> getPictureRecordsByCat(String cat){
        String sql = "SELECT picID, path, preview "
                + "FROM picture WHERE category = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, cat);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> recs = new ArrayList<>(10);
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    recs.add(new PictureRecord(id, path, preview));
                }
                recs.trimToSize();
                return recs;
            } else {
                recs.trimToSize();
                return recs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }
    
    /**
     * gets the paths of all pictures by NULL category
     * 
     * @return      an ArrayList-String- of id and paths, or an empty list or null
     */
    public ArrayList<PictureRecord> getPictureRecordsByCatNULL(){
        String sql = "SELECT picID, path, preview "
                + "FROM picture WHERE category IS NULL";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> recs = new ArrayList<>(10);
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    recs.add(new PictureRecord(id, path, preview));
                }
                recs.trimToSize();
                return recs;
            } else {
                recs.trimToSize();
                return recs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }
    
    /**
     * gets the paths of all pictures
     * 
     * @return      an ArrayList-String- of paths, or an empty list or null
     */
    public ArrayList<String> getAllPicturePaths(){
        String sql = "SELECT path "
                + "FROM picture";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<String> paths = new ArrayList<>(10);
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    String path = rs.getString("path");
                    paths.add(path);
                }
                paths.trimToSize();
                return paths;
            } else {
                paths.trimToSize();
                return paths;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }
    
        /**
     * gets a picture object
     * 
     * @param id    the id of the picture
     * @return      the picture of type Picture object
     */
    public Picture getPicture(int id){
        String sql = "SELECT picID, name, date_taken, place_taken, size, category, date_insertion, last_modified, path, preview "
                + "FROM picture WHERE picID = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, id);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                int p_id = rs.getInt("picID");
                String name = rs.getString("name");
                String place = rs.getString("place_taken");
                java.sql.Date date = rs.getDate("date_taken");
                int size = rs.getInt("size");
                String cat = rs.getString("category");
                java.sql.Timestamp insertion = rs.getTimestamp("date_insertion");
                java.sql.Timestamp modify = rs.getTimestamp("last_modified");
                String path = rs.getString("path");
                byte[] preview = rs.getBytes("preview");
                if(cat == null){
                    return new Picture(p_id, name, place, date, size, insertion, modify, path, preview);
                } else {
                    return new Picture(p_id, name, place, date, size, cat, insertion, modify, path, preview);
                } 
            } else {
                return null;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }
    
    /**
     * gets a picture object
     * 
     * @param name_pic  the name of the picture
     * @return          the picture of type Picture object
     */
    public Picture getPicture(String name_pic){
        String sql = "SELECT picID, name, date_taken, place_taken, size, category, date_insertion, last_modified, path, preview "
                + "FROM picture WHERE name = ?";
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, name_pic);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                int p_id = rs.getInt("picID");
                String name = rs.getString("name");
                String place = rs.getString("place_taken");
                java.sql.Date date = rs.getDate("date_taken");
                int size = rs.getInt("size");
                String cat = rs.getString("category");
                java.sql.Timestamp insertion = rs.getTimestamp("date_insertion");
                java.sql.Timestamp modify = rs.getTimestamp("last_modified");
                String path = rs.getString("path");
                byte[] preview = rs.getBytes("preview");
                if(cat == null){
                    return new Picture(p_id, name, place, date, size, insertion, modify, path, preview);
                } else {
                    return new Picture(p_id, name, place, date, size, cat, insertion, modify, path, preview);
                } 
            } else {
                return null;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading a picture");
            return null;
        }
    }

/* ******************* END CREATION AND MODIFICATION ******************* */ 

    
/* ******************* COMMENT CREATION AND MODIFICATION ******************* */
    
    /**
     * get all the comments of a picture
     * 
     * @param id    the picture id
     * @return      the list of comments
     */
    public ArrayList<Comment> getCommentByPicID(int id){
        String sql = "SELECT commId, picID, author, text, added "
                + "FROM comment WHERE picId = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            ArrayList<Comment> comms = new ArrayList<>();
            p.setInt(1, id);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int c_id = rs.getInt("commID");
                    int p_id = rs.getInt("picID");
                    String auth = rs.getString("author");
                    String text = rs.getString("text");
                    java.sql.Timestamp added = rs.getTimestamp("added");
                    comms.add(new Comment(c_id, p_id, auth, text, added));
                }
                comms.trimToSize();
                return comms;
            } else {
                comms.trimToSize();
                return comms;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading comments");
            return null;
        } 
    }
    
    /**
     * adds a comment to a picture
     * 
     * @param picID     the picture to which to add a comment
     * @param author    the author of the 
     * @param text      the text of the comment
     */
    public void commentAdd(int picID, String author, String text){
        String sql = "INSERT INTO comment (picID, author, text) VALUES (?,?,?)";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, picID);
            p.setString(2, author);
            p.setString(3, text);
            
            p.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException ex){
            showError(ex, "SQL Integrity Error when adding comments");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when adding comments");
        }
    }
    
    /**
     * edits a comment
     * 
     * @param commID    the comment to edit
     * @param author    the edited author for the comment
     */
    public void commentEditAuth(int commID, String author){
        String sql = "UPDATE comment SET author = ? WHERE commID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, author);
            p.setInt(2, commID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing comments");
        }
    }
    
    /**
     * edits a comment
     * 
     * @param commID    the comment to edit
     * @param text      the edited text for the comment
     */
    public void commentEditText(int commID, String text){
        String sql = "UPDATE comment SET text = ? WHERE commID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, text);
            p.setInt(2, commID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing comments");
        }
    }
    
    /**
     * edits a comment
     * 
     * @param commID    the comment to edit
     * @param author    the new author
     * @param text      the edited text for the comment
     */
    public void commentEdit(int commID, String author, String text){
        String sql = "UPDATE comment SET author = ? , text = ? WHERE commID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, author);
            p.setString(2, text);
            p.setInt(3, commID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing comments");
        }
    }
    
    /**
     * deletes a comment
     * 
     * @param commID    the ID of the comment
     */
    public void commentDelete(int commID){
        String sql = "DELETE FROM comment WHERE commID = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, commID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting comments");
        }
    }
    
/* ******************* END COMMENT CREATION AND MODIFICATION ******************* */
    
    
/* ******************* ANNOTATION CREATION AND MODIFICATION ******************* */
    
    /**
     * adds an annotation to a picture
     * 
     * @param picID     the picture to which to add an annotation
     * @param text      the text of the annotation
     */
    public void annotationAdd(int picID, String text){
        annotationAdd(picID, text, 0.0, 0.0, 12, Color.WHITE.toString());
    }
    
    /**
     * adds an annotation to a picture
     * 
     * @param picID     the picture to which to add an annotation
     * @param text      the text of the annotation
     * @param x         the x-coord of the annotation
     * @param y         the y-coord of the annotation
     * @param size      the font-size of the annotation
     * @param color     the font-colour of the annotation
     */
    public boolean annotationAdd(int picID, String text, double x, double y, int size, String color){
        String sql = "INSERT INTO annotation (picID, text, x, y, size, color) VALUES (?,?,?,?,?,?)";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, picID);
            p.setString(2, text);
            p.setDouble(3, x);
            p.setDouble(4, y);
            p.setInt(5, size);
            p.setString(6, color);
            
            p.executeUpdate();
            
            return true;
        } catch(SQLIntegrityConstraintViolationException ex){
            showError(ex, "SQL Integrity Error when adding annotation");
            return false;
        } catch (SQLException ex) {
            showError(ex, "SQL Error when adding annotation");
            return false;
        }
    }
    
    
    /**
     * edits an annotation
     * 
     * @param annID     the annotation to edit
     * @param text      the edited text for the annotation
     */
    public void annotationEditText(int annID, String text){
        String sql = "UPDATE annotation SET text = ? WHERE annID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, text);
            p.setInt(2, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing annotation text");
        }
    }
    
    /**
     * edits an annotation
     * 
     * @param annID     the annotation to edit
     * @param color     the edited colour for the annotation
     */
    public void annotationEditColor(int annID, String color){
        String sql = "UPDATE annotation SET color = ? WHERE annID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, color);
            p.setInt(2, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing annotation colour");
        }
    }
    
    /**
     * edits an annotation
     * 
     * @param annID     the annotation to edit
     * @param x         the new coord x
     * @param y         the new coord y
     */
    public void annotationEdit(int annID, double x, double y){
        String sql = "UPDATE annotation "
                + "SET x = ?, y = ? "
                + "WHERE annID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setDouble(1, x);
            p.setDouble(2, y);
            p.setInt(3, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing annotation coordinates");
        }
    }
    
    /**
     * edits an annotation
     * 
     * @param annID     the annotation to edit
     * @param size      the new size
     */
    public void annotationEdit(int annID, int size){
        String sql = "UPDATE annotation "
                + "SET size = ? "
                + "WHERE annID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, size);
            p.setInt(2, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing annotation size");
        }
    }
    /**
     * edits an annotation
     * 
     * @param annID     the annotation to edit
     * @param text      the edited text for the annotation
     * @param x         the new coord x
     * @param y         the new coord y
     * @param size      the new size
     */
    public void annotationEdit(int annID, String text, int x, int y, int size){
        String sql = "UPDATE annotation "
                + "SET text = ?, x = ?, y = ?, size = ? "
                + "WHERE annID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, text);
            p.setInt(2, x);
            p.setInt(3, y);
            p.setInt(4, size);
            p.setInt(5, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when editing annotation");
        }
    }
    
    /**
     * deletes an annotation
     * 
     * @param annID     the ID of the annotation
     */
    public void annotationDelete(int annID){
        String sql = "DELETE FROM annotation WHERE annID = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, annID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting annotation");
        }
    }
    
    /**
     * get annotation ID by text
     * 
     * @param text  the text to look by
     * @return      the id or -1 if not found
     */
    public int annotationGetID(String text){
        String sql = "SELECT annID FROM annotation WHERE text = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, text);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                int a_id = rs.getInt("annID");

                return a_id;
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading annotation");
            return -1;
        }
    }
    
    /**
     * get all the annotation of the given picture
     * @param id        the id of the picture
     * @return          the list of annotations or empty list
     */
    public ArrayList<Annotation> getAllAnnotationsOfPicture(int id) {
        String sql = "SELECT annId, picID, text, x, y, size, color, added "
                + "FROM annotation WHERE picId = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, id);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<Annotation> anns = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int a_id = rs.getInt("annID");
                    int p_id = rs.getInt("picID");
                    String text = rs.getString("text");
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    int size = rs.getInt("size");
                    String color = rs.getString("color");
                    java.sql.Timestamp added = rs.getTimestamp("added");
                    anns.add(new Annotation(a_id, p_id, text, x, y, size, color, added));
                }
                anns.trimToSize();
                return anns;
            } else {
                anns.trimToSize();
                return anns;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading annotation");
            return null;
        }
    }
    
/* ******************* END ANNOTATION CREATION AND MODIFICATION ******************* */
    
    
/* ******************* LABELPIC CREATION AND MODIFICATION ******************* */
    
    /**
     * creates a relation between a picture and a label 
     * 
     * @param label     the label
     * @param picture   the picture's id
     */
    public void labelpicAdd(String label, int picture){
        if(labelpicGetLabelPic(label, picture) != null){
            return;
        } 
        
        String sql = "INSERT INTO labelpic (label, picture) VALUES (?,?)";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            p.setInt(2, picture);
            
            p.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException ex){
            showError(ex, "SQL Integrity Error when creading label for picture");
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creading label for picture");
        }
    }
    
    /**
     * modify the label and picture of a labelpic entry
     * 
     * @param lpicID    the id of the labelpic to edit
     * @param label     the new label
     * @param picture   the new picture
     */
    public void labelpicEdit(int lpicID, String label, int picture){
        String sql = "UPDATE labelpic SET label = ?, picture = ? WHERE lpicID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            p.setInt(2, picture);
            p.setInt(3, lpicID);
            
            p.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex){
            showError(ex, "SQL Integrity Error when editing label for picture");
        } catch (SQLException ex) {
            showError(ex, "SQL  Error when editing label for picture");
        }
    }
    
    /**
     * delete a labelpic entry
     * 
     * @param lpicID    the ID of the labelpic
     */
    public void labelpicDelete(int lpicID){
        String sql = "DELETE FROM labelpic WHERE lpicID = ? ";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setInt(1, lpicID);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Integrity Error when deleting label for picture");
        }
    }
    
    /**
     * delete a labelpic entry
     * 
     * @param label     the label of the labelpic
     * @param picture   the picture of the labelpic
     */
    public void labelpicDelete(String label, int picture){
        String sql = "DELETE FROM labelpic WHERE label = ? AND picture = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            p.setInt(2, picture);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting label for picture");
        }
    }
    
    /**
     * delete all labelpic entry of picture
     * 
     * @param picture   the picture of the labelpic
     */
    public void labelpicDeleteByPic(int picture){
        String sql = "DELETE FROM labelpic WHERE picture = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);

            p.setInt(1, picture);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting label for picture");
        }
    }
    
    /**
     * delete all labelpic entry of label
     * 
     * @param label the label of the labelpic
     */
    public void labelpicDeleteByLabel(String label){
        String sql = "DELETE FROM labelpic WHERE label = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, label);
            
            p.executeUpdate();
        } catch (SQLException ex) {
            showError(ex, "SQL Error when deleting label for picture");
        }
    }
    
    /**
     * get a LabelPic object by ID
     * 
     * @param lpicID    the ID of the labelpic obj
     * @return          the labelpic objs 
     */
    public LabelPic labelpicGetLabelPic(int lpicID){
        String sql = "SELECT lpicID, label, picture "
                + "FROM labelpic WHERE lpicID = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, lpicID);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                int lp_id = rs.getInt("lpicID");
                String lab = rs.getString("label");
                int pic = rs.getInt("picture");
                return new LabelPic(lp_id, lab, pic);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label for picture");
            return null;
        }
    }
    
    /**
     * get a LabelPic object by ID
     * 
     * @param label     the label to look
     * @param picture   the picture to look
     * @return          the labelpic objs 
     */
    public LabelPic labelpicGetLabelPic(String label, int picture){
        String sql = "SELECT lpicID, label, picture "
                + "FROM labelpic WHERE label = ? AND picture = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, label);
            p.setInt(2, picture);
            
            ResultSet rs = p.executeQuery();
            
            if(rs.isBeforeFirst()){
                rs.next();
                int lp_id = rs.getInt("lpicID");
                String lab = rs.getString("label");
                int pic = rs.getInt("picture");
                return new LabelPic(lp_id, lab, pic);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label for picture");
            return null;
        }
    }
    
    /**
     * get all LabelPic objects by label
     * 
     * @param label     the label to search by
     * @return          the list of labelpic objs 
     */
    public ArrayList<LabelPic> labelpicGelLabelPic(String label){
        String sql = "SELECT lpicID, label, picture "
                + "FROM labelpic WHERE label = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, label);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<LabelPic> lpics = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int lpID = rs.getInt("lpicID");
                    int pic = rs.getInt("picture");
                    lpics.add(new LabelPic(lpID, label, pic));
                }
                lpics.trimToSize();
                return lpics;
            } else {
                lpics.trimToSize();
                return lpics;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label for picture");
            return null;
        }
    }
    
    /**
     * get all LabelPic objects by picture
     * 
     * @param picture    the picture to search by
     * @return          the list of labelpic objs 
     */
    public ArrayList<LabelPic> labelpicGelLabelPic(int picture){
        String sql = "SELECT lpicID, label, picture "
                + "FROM labelpic WHERE picture = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, picture);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<LabelPic> lpics = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int lpID = rs.getInt("lpicID");
                    String lab = rs.getString("label");
                    lpics.add(new LabelPic(lpID, lab, picture));
                }
                lpics.trimToSize();
                return lpics;
            } else {
                lpics.trimToSize();
                return lpics;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label for picture");
            return null;
        }
    }
    
    /**
     * get all labelpic by objects by label
     * [duplicate], P., Cornelissen, P. and vijayvargiya, G. (2019). 
     * PreparedStatement with list of parameters in a IN clause. 
     * [online] Stack Overflow. 
     * Available at: https://stackoverflow.com/questions/3107044/preparedstatement-with-list-of-parameters-in-a-in-clause 
     * [Accessed 24 Apr. 2019].
     * 
     * 
     * @param label     the labels to look by
     * @return          the labelpic
     */
    public ArrayList<LabelPic> labelpicGelLabelPic(ArrayList<String> label){
        StringBuilder builder = new StringBuilder();

        for( int i = 0 ; i < label.size(); i++ ) {
            builder.append("?,");
        }
        
        String sql = "SELECT lpicID, label, picture "
                + "FROM labelpic WHERE label IN ("
                + builder.deleteCharAt(builder.length() -1 ).toString()
                + ")";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
        
            for (int i = 0; i < label.size(); i++) {
                p.setString((i+1), label.get(i));
            }
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<LabelPic> lpics = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int lpID = rs.getInt("lpicID");
                    String lab = rs.getString("label");
                    int pic = rs.getInt("picture");
                    lpics.add(new LabelPic(lpID, lab, pic));
                }
                lpics.trimToSize();
                return lpics;
            } else {
                lpics.trimToSize();
                return lpics;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label for picture");
            return null;
        }
    } 
    
/* ******************* END LABELPIC CREATION AND MODIFICATION ******************* */   
   
/* ************************** LABELPIC & PICTURE SELECT ************************** */    
   
    /**
     * gets all labels of picture
     * 
     * @param picID     the picture's id
     * @return          list of labels or empty list
     */
    public ArrayList<String> getAllLabelsOfPic(int picID){
        String sql = "SELECT DISTINCT label "
                + "FROM labelpic WHERE picture = ?";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, picID);
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<String> labs = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    String lab = rs.getString("label");
                    labs.add(lab);
                }
                labs.trimToSize();
                return labs;
            } else {
                labs.trimToSize();
                return labs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading label of picture");
            return null;
        }
    }
    
    /**
     * get all pictures by category and labels
     * 
     * @param category      the category of the pictures
     * @param labs          the labels of the pictures
     * @return              the list of pictures or empty list
     */
    public ArrayList<PictureRecord> getPictureByCatAndLabel(String category, ArrayList<String> labs){
        StringBuilder builder = new StringBuilder();

        for( int i = 0 ; i < labs.size(); i++ ) {
            builder.append("?,");       // creates the string for the labels e.g (?,?,?,?)
        }
        
        String sql = "SELECT DISTINCT picture.* "
                + "FROM picture "
                + "INNER JOIN labelpic "
                + "ON picture.picID = labelpic.picture "
                + "WHERE picture.category = ? AND labelpic.label IN ("
                + builder.deleteCharAt(builder.length() -1 ).toString()
                + ")";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);
            
            p.setString(1, category);
            for (int i = 0; i < labs.size(); i++) {
                p.setString((i+2), labs.get(i));
            }
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> prs = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int p_id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    prs.add(new PictureRecord(p_id, path, preview));
                }
                prs.trimToSize();
                return prs;
            } else {
                prs.trimToSize();
                return prs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading pictues of label");
            return null;
        }
    }
    
    /**
     * get all pictures by labels
     * 
     * @param labs      the labels of the pictures
     * @return          the list of pictures or empty list
     */
    public ArrayList<PictureRecord> getAllPictureByLabel(ArrayList<String> labs){
        StringBuilder builder = new StringBuilder();

        for( int i = 0 ; i < labs.size(); i++ ) {
            builder.append("?,");
        }
        
        String sql = "SELECT DISTINCT picture.* "
                + "FROM picture "
                + "INNER JOIN labelpic "
                + "ON picture.picID = labelpic.picture "
                + "WHERE labelpic.label IN ("
                + builder.deleteCharAt(builder.length() -1 ).toString()
                + ")";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);

            for (int i = 0; i < labs.size(); i++) {
                p.setString((i+1), labs.get(i));
            }
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> prs = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int p_id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    prs.add(new PictureRecord(p_id, path, preview));
                }
                prs.trimToSize();
                return prs;
            } else {
                prs.trimToSize();
                return prs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading pictues of label");
            return null;
        }
    }
    
    /**
     * get all pictures by category = NULL and labels
     * 
     * @param labs          the labels of the pictures
     * @return              the list of pictures or empty list
     */
    public ArrayList<PictureRecord> getPictureByCatNULLAndLabel(ArrayList<String> labs){
        StringBuilder builder = new StringBuilder();

        for( int i = 0 ; i < labs.size(); i++ ) {
            builder.append("?,");
        }
        
        String sql = "SELECT DISTINCT picture.* "
                + "FROM picture "
                + "INNER JOIN labelpic "
                + "ON picture.picID = labelpic.picture "
                + "WHERE picture.category IS NULL  AND labelpic.label IN ("
                + builder.deleteCharAt(builder.length() -1 ).toString()
                + ")";
        
        try {
            PreparedStatement p = c.prepareStatement(sql);

            for (int i = 0; i < labs.size(); i++) {
                p.setString((i+1), labs.get(i));
            }
            
            ResultSet rs = p.executeQuery();
            
            ArrayList<PictureRecord> prs = new ArrayList<>();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    int p_id = rs.getInt("picID");
                    String path = rs.getString("path");
                    byte[] preview = rs.getBytes("preview");
                    prs.add(new PictureRecord(p_id, path, preview));
                }
                prs.trimToSize();
                return prs;
            } else {
                prs.trimToSize();
                return prs;
            }
        } catch (SQLException ex) {
            showError(ex, "SQL Error when reading pictues of label");
            return null;
        }
    }
    
    
    
    
/* **************************   END LABELPIC & PICTURE SELECT   ************************** */    
    
    
    
    
/* **************************   DATABASE AND TABLE CREATION   ************************** */

    /**
     * create table category in database 
     */
    private void createTableCategoty(){
        String sql = "CREATE TABLE IF NOT EXISTS category( "
                + "catID INT NOT NULL AUTO_INCREMENT , "
                + "category VARCHAR(128) NOT NULL UNIQUE, "
                + "PRIMARY KEY (catID) "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table ");
        }
    }
    
    /**
     * create table label in database 
     */
    private void createTableLabel(){
        String sql = "CREATE TABLE IF NOT EXISTS label( "
                + "labID INT NOT NULL AUTO_INCREMENT , "
                + "label VARCHAR(128) NOT NULL UNIQUE , "
                + "PRIMARY KEY (labID) "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table label");
        }
    }
    
    /**
     * create table picture in database 
     */
    private void createTablePicture(){
        String sql = "CREATE TABLE IF NOT EXISTS picture ("
                + "picID INT NOT NULL AUTO_INCREMENT , "
                + "name VARCHAR(128) NOT NULL , "
                + "date_taken DATE NOT NULL , "
                + "place_taken VARCHAR(128) NOT NULL , "
                + "size INT NOT NULL , "
                + "category VARCHAR(128) NULL , "
                + "date_insertion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , "
                + "last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "path VARCHAR(2048) NOT NULL , "
                + "preview MEDIUMBLOB NULL , "
                + "PRIMARY KEY (picID) , "
                + "FOREIGN KEY (category) REFERENCES category(category) ON UPDATE CASCADE "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table picture");
        }
    }
    
    /**
     * create table labelpic in database 
     */
    private void createTableLabelPic(){
        String sql = "CREATE TABLE IF NOT EXISTS labelpic( "
                + "lpicID INT NOT NULL AUTO_INCREMENT , "
                + "label VARCHAR(128) NOT NULL , "
                + "picture INT NOT NULL , "
                + "PRIMARY KEY (lpicID) , "
                + "FOREIGN KEY (label) REFERENCES label(label) ON UPDATE CASCADE , "
                + "FOREIGN KEY (picture) REFERENCES picture(picID)  "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table labelpic");
        }
    }
    
    /**
     * create table annotation in database 
     */
    private void createTableAnnotation(){
        String sql = "CREATE TABLE IF NOT EXISTS annotation ("
                + "annID INT NOT NULL AUTO_INCREMENT , "
                + "picID INT NOT NULL , "
                + "text VARCHAR(128) NOT NULL UNIQUE , "
                + "x DOUBLE NOT NULL , "
                + "y DOUBLE NOT NULL , "
                + "size INT NOT NULL , "
                + "color VARCHAR(10) NOT NULL , "
                + "added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , "
                + "PRIMARY KEY (annID) , "
                + "FOREIGN KEY (picID) REFERENCES picture(picID) "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table annotation");
        }
    }
    
    /**
     * create table comment in database 
     */
    private void createTableComment(){
        String sql = "CREATE TABLE IF NOT EXISTS comment ("
                + "commID INT NOT NULL AUTO_INCREMENT , "
                + "picID INT NOT NULL , "
                + "author VARCHAR(128) NOT NULL , "
                + "text TEXT NOT NULL , "
                + "added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , "
                + "PRIMARY KEY (commID) , "
                + "FOREIGN KEY (picID) REFERENCES picture(picID) "
                + ") ENGINE = InnoDB;";
        
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            showError(ex, "SQL Error when creating table comment");
        }
    }
    
    private void createTables(){
        createTableCategoty();
        createTableLabel();
        createTablePicture();
        createTableLabelPic();
        createTableAnnotation();
        createTableComment();
    }
    
    private void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
}
