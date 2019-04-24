/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.annotation;

import homealbum.SQLConnection;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for adding new annotations
 *
 * @author User
 */
public class AddAnnotationController implements Initializable {

    private boolean annoted;
    
    @FXML
    private AnchorPane apAnnMain;

    @FXML
    private Label lbPicPath;

    @FXML
    private TextArea taAnn;

    @FXML
    private Label lbAnnErr;

    /**
     * handle events of button save in add annotation
     * adds annotation to database then closes window
     * 
     * @param event the click event
     */
    @FXML
    void handleAddAnnotation(ActionEvent event) {
        String ann = taAnn.getText();
        if(ann.equalsIgnoreCase("")){       // if annotation is empty
            lbAnnErr.setText("Insert an annotation!");  // shows error message
        } else {    // adds annotation to the database
            annoted = true;                                                 // reords the choice to save annotation
            SQLConnection conn = SQLConnection.getConnection();             // dabase connector
            conn.annotationAdd(conn.getPictureId(lbPicPath.getText()), ann);// annoation insert action
            closeWindow();                                                  // closes window upon completition
        }
    }

    /**
     * handle events of button cancel in add annotation
     * closes window
     * 
     * @param event the click event
     */
    @FXML
    void handleCancelAnnotation(ActionEvent event) {
        annoted = false;            // reords the choice to not to save annotation  
        closeWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * returns whether a new annotation has been added or not 
     * @return 
     */
    public boolean isAnnoted(){
        return annoted;
    }

    /** 
     * set to which picture to add annotation to
     * @param path      the path of the picture
     */
    public void setPicture(String path){
        lbPicPath.setText(path);
    }
    
    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apAnnMain.getScene().getWindow()).close();
    }
    
}
