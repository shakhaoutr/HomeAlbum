/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.annotation;

import homealbum.SQLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for editing annotations
 *
 * @author Shakhaout Rahman
 */
public class EditAnnotationController implements Initializable {

    private boolean edited = false;     // annotation modified
    private int annID = -1;             // annotation id
    private String oldAnn = "";         // old annotation text

    @FXML
    private AnchorPane apEditAnn;

    @FXML
    private Label lbPicPath;

    @FXML
    private TextArea taAnn;

    /**
     * interrupt annoation edit
     * closes the window without doing nothing
     * 
     * @param event 
     */
    @FXML
    void handleEditCancel(ActionEvent event) {
        edited = false;
        closeWindow();
    }

    /**
     * saves in database the edits to the annotation
     * 
     * @param event     click event of save button
     */
    @FXML
    void handleEditConfirm(ActionEvent event) {
        String newComm = taAnn.getText();
        if(!(annID == -1 || newComm.equalsIgnoreCase("") || newComm.equals(oldAnn))){   // if edit is valid
            SQLConnection conn = SQLConnection.getConnection();
            conn.annotationEditText(annID, newComm);                // modify the annotation
            edited = true;                                          // confirm edit action
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
     * close the window
     */
    private void closeWindow(){
        ((Stage)apEditAnn.getScene().getWindow()).close();
    }
    
    /**
     * return whether the annotation has been edit
     * 
     * @return      true - edited; false - not edited
     */
    public boolean isEdited(){
        return edited;
    }
    
    /**
     * sets the picture's path
     * 
     * @param path  the picture's path
     */
    public void setPicpath(String path){
        lbPicPath.setText(path);
    }
    
    /**
     * sets the value of the annotation
     * 
     * @param id        the annotation id
     * @param c         the annotation text
     */
    public void setAnnotation(int id,String c){
        oldAnn = c;
        taAnn.setText(oldAnn);
        annID = id;
    }    
    
}
