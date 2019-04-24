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
 * FXML Controller class for delete annotation
 *
 * @author Shakhaout Rahman
 */
public class DeleteAnnotationController implements Initializable {

    private boolean deleted = false;
    private int annID = -1;

    @FXML
    private AnchorPane apAnnDel;

    @FXML
    private TextArea taAnn;

    @FXML
    private Label lbPicPath;

    /**
     * handles delete action interruptions
     * closes the window without doing anything
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteCancel(ActionEvent event) {
        deleted = false;
        closeWindow();
    }

    /**
     * deletes the chosen annotation
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteConfirm(ActionEvent event) {
        if(!((annID == -1) || lbPicPath.getText().equalsIgnoreCase(""))){ // if annotation exist
            SQLConnection conn = SQLConnection.getConnection();
            conn.annotationDelete(annID);                       // delete annotation from database
            deleted = true;                                     // register annotation deleted action
            closeWindow();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apAnnDel.getScene().getWindow()).close();
    }

    /**
     * registers whether annotation has been deleted or not 
     * @return 
     */
    public boolean isDeleted(){
        return deleted;
    }
    
    /**
     * set the details of the annotation
     * @param aid       the annotation's id
     * @param s         the annotation's text
     */
    public void setTextAnnotation(int aid, String s){
        annID = aid;
        taAnn.setText(s);
    }
    
    /**
     * sets the picture's path
     * @param path      the path
     */
    public void setPicPath(String path){
        lbPicPath.setText(path);
    }
}
