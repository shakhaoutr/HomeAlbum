/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homealbum;

import org.apache.commons.io.FilenameUtils;
import view.gui.AlertWindow;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Settings;


/**
 * offers utility methods to manage windows in app
 * @author Shakhaout Rahman
 */
public class Utilities {
    
    /**
     * loads the file given the path
     * 
     * @param path      the file path
     * @return          the file object
     */
    public static File getImgFile(String path){
        return new File(path);
    }
    
    /**
     * gets the image given the path
     * 
     * @param path      the file path
     * @return          the FX Image
     */
    public static Image getImage(String path){
        return new Image(getImgFile(path).toURI().toString());
    }
    
    public static boolean testSQLConnection(){
        Settings s = Settings.getSettings();
        
        Connection c;
        try {
            Class.forName(s.DB_CLASS);
            c = DriverManager.getConnection(s.DB_DRIVER+"//"+s.DB_HOST+"/"+
                    s.DB_NAME+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
                    s.DB_USER, s.DB_PASSWD);
            
            return true;
        } catch (ClassNotFoundException ex) {
            AlertWindow.display("SQL Class Not Found", ex.getMessage());
            return false;
        } catch (SQLException ex) {
            AlertWindow.display("SQL Error", ex.getMessage());
            return false;
        }
    }
    
    /**
     * converts the given date in string to SQL Date object
     * @param date      the date
     * @return          the SQL date object
     */
    public static java.sql.Date getSQLDate(String date){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d = df.parse(date);
            return new java.sql.Date(d.getTime());
        } catch (ParseException ex) {
            showError(ex, "Error during the conversion of the date in sql");
            return null;
        }
    }
    
    /**
     * returns the stage of the given component
     * 
     * @param ap    the component
     * @return      the parent stage of the component
     */
    public static Stage getParentStage(AnchorPane ap){
        Stage root = (Stage) ap.getScene().getWindow();
        return root;
    }
    
    /**
     * returns the stage of the given component
     * 
     * @param hb    the component
     * @return      the parent stage of the component
     */
    public static Stage getParentStage(HBox hb){
        Stage root = (Stage) hb.getScene().getWindow();
        return root;
    }
    
    /**
     * returns the stage of the given component
     * 
     * @param vb    the component
     * @return      the parent stage of the component
     */
    public static Stage getParentStage(VBox vb){
        Stage root = (Stage) vb.getScene().getWindow();
        return root;
    }
    
    /**
     * returns the stage of the given component
     * 
     * @param bp    the component
     * @return      the parent stage of the component
     */
    public static Stage getParentStage(BorderPane bp){
        Stage root = (Stage) bp.getScene().getWindow();
        return root;
    }
    
    /**
     * returns the stage of the given component
     * 
     * @param sp    the component
     * @return      the parent stage of the component
     */
    public static Stage getParentStage(SplitPane sp){
        Stage root = (Stage) sp.getScene().getWindow();
        return root;
    }
    
    /**
     * blocks the window until all child windows haven't been closed
     * 
     * @param sp    the component whose windows must be blocked
     * @param s     the child window
     */
    public static void lockOwnerWindow(SplitPane sp, Stage s){
        s.initOwner(Utilities.getParentStage(sp));
        s.initModality(Modality.WINDOW_MODAL);
    }
    
    /**
     * blocks the window until all child windows haven't been closed
     * 
     * @param ap    the component whose windows must be blocked
     * @param s     the child window
     */
    public static void lockOwnerWindow(AnchorPane ap, Stage s){
        s.initOwner(Utilities.getParentStage(ap));
        s.initModality(Modality.WINDOW_MODAL);
    }
    
    /**
     * blocks the window until all child windows haven't been closed
     * 
     * @param hb    the component whose windows must be blocked
     * @param s     the child window
     */
    public static void lockOwnerWindow(HBox hb, Stage s){
        s.initOwner(Utilities.getParentStage(hb));
        s.initModality(Modality.WINDOW_MODAL);
    }
    
    /**
     * blocks the window until all child windows haven't been closed
     * 
     * @param vb    the component whose windows must be blocked
     * @param s     the child window
     */
    public static void lockOwnerWindow(VBox vb, Stage s){
        s.initOwner(Utilities.getParentStage(vb));
        s.initModality(Modality.WINDOW_MODAL);
    }
    
    /**
     * blocks the window until all child windows haven't been closed
     * 
     * @param bp    the component whose windows must be blocked
     * @param s     the child window
     */
    public static void lockOwnerWindow(BorderPane bp, Stage s){
        s.initOwner(Utilities.getParentStage(bp));
        s.initModality(Modality.WINDOW_MODAL);
    }
    
    /**
     * get the base name of a file, without extension
     * 
     * @param f     the file
     * @return      the name of the file
     */
    public static String getFileName(File f){
        return FilenameUtils.removeExtension(f.getName());
    }
    
    /**
     * get the base name of a file, without extension
     * 
     * @param f     the file name
     * @return      the name of the file
     */
    public static String getFileName(String f){
        int i = f.indexOf(".jpeg");
        if(i != -1){
            return f.substring(0, i);
        }
        i = f.indexOf(".jpg");
        if(i != -1){
            return f.substring(0, i);
        }
        i = f.indexOf(".png");
        if(i != -1){
            return f.substring(0, i);
        }
        return f;
    }
    
    /**
     * returns the extentions of a file
     * 
     * @param f     the file
     * @return      the extension "."+extension
     */
    public static String getFileExt(File f){
        return "."+FilenameUtils.getExtension(f.getName());
    }
    
    /**
     * returns the extentions of a file name
     * @param f     the file name
     * @return      the extension "."+extension
     */
    public static String getFileExt(String f){
        int i = f.indexOf(".jpeg");
        if(i != -1){
            return ".jpeg";
        }
        i = f.indexOf(".jpg");
        if(i != -1){
            return ".jpg";
        }
        i = f.indexOf(".png");
        if(i != -1){
            return ".png";
        }
        return f;
    }
    
    private static void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
}
