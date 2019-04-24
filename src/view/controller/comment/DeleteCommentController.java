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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI delete comment
 *
 * @author Shakhaout Rahman
 */
public class DeleteCommentController implements Initializable {

    private boolean deleted = false;
    private int commID = -1;
            
    @FXML
    private AnchorPane apCommDel;
    
    @FXML
    private TextArea taComment;

    @FXML
    private Label lbPicName;

    /**
     * handle event interrupt delete comment
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteCancel(ActionEvent event) {
        deleted = false;
        closeWindow();
    }

    /**
     * handle event delete comment
     * if user conforms, the comment is deleted from database
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteConfirm(ActionEvent event) {
        if(!((commID == -1) || lbPicName.getText().equalsIgnoreCase(""))){
            SQLConnection conn = SQLConnection.getConnection();
            conn.commentDelete(commID);
            deleted = true;
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
        ((Stage)apCommDel.getScene().getWindow()).close();
    }

    /**
     * keeps track of whether the comment has been deleted
     * 
     * @return      true - deleted; false - not deleted
     */
    public boolean isDeleted(){
        return deleted;
    }
    
    /**
     * sets the comments parameters
     * 
     * @param cid       the comment's ID in database
     * @param s         the comment's content
     */
    public void setTextComment(int cid, String s){
        commID = cid;
        taComment.setText(s);
    }
    
    /**
     * register which picture's comment is being deleted
     * 
     * @param name  the picture's name
     */
    public void setPicName(String name){
        lbPicName.setText(name);
    }
    
}
