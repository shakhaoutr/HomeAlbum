/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.comment;

import homealbum.SQLConnection;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


/**
 * FXML Controller class for GUI comment edit
 *
 * @author Shakhaout Rahman
 */
public class EditCommentController implements Initializable {
    
    private boolean edited = false;
    private int commID = -1;
    private String oldAuth = "";
    private String oldComm = "";

    @FXML
    private AnchorPane apEditComm;

    @FXML
    private Label lbPicName;

    @FXML
    private TextField tfAuth;

    @FXML
    private TextArea taComm;

    /**
     * handle event interrupt edit comment
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    void handleEditCancel(ActionEvent event) {
        edited = false;
        closeWindow();
    }

    /**
     * handle event edit comment
     * if comment is valid, edits comment in the database and
     * 
     * @param event     the click event
     */
    @FXML
    void handleEditConfirm(ActionEvent event) {
        String newAuth = tfAuth.getText();
        String newComm = taComm.getText();
        if(commID != -1){                                                           // checks if comment's id is valid
            SQLConnection conn = SQLConnection.getConnection();
            if(!newAuth.equalsIgnoreCase("") || !newAuth.equals(oldComm)){          // if new author is valid and changed
                if(!newComm.equalsIgnoreCase("") || !newComm.equals(oldComm)){      // new comment is valid and changed
                    conn.commentEdit(commID, newAuth, newComm);                     // edits both
                    edited = true;
                }
                conn.commentEditAuth(commID, newAuth);                              // edits only author
                edited = true;
            } else if(!newComm.equalsIgnoreCase("") || !newComm.equals(oldComm)){   // if only comments has changed
                conn.commentEditText(commID, newComm);                              // edit only comment
                edited = true;
            }
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
     * close the windows
     */
    private void closeWindow(){
        ((Stage)apEditComm.getScene().getWindow()).close();
    }
    
    /**
     * keeps track of whether the comment has been edited
     * 
     * @return      true - edited; false - not edited
     */
    public boolean isEdited(){
        return edited;
    }
    
    /**
     * sets pic name
     * @param name      the picture's name
     */
    public void setPicName(String name){
        lbPicName.setText(name);
    }
    
    /**
     * sets the comments parameters
     * 
     * @param id        the comment's ID in database
     * @param author    the comment's author
     * @param comment   the comment's content         
     */
    public void setComment(int id, String author, String comment){
        commID = id;
        oldAuth = author;
        oldComm = comment;
        
        tfAuth.setText(oldAuth);
        taComm.setText(oldComm);
    }
    
}
