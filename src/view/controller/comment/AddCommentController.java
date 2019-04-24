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
 * FXML Controller class for the GUI add comment
 *
 * @author Shakhaout Rahman
 */
public class AddCommentController implements Initializable {

    private boolean commented = false;
    private int id;
    
    @FXML
    private AnchorPane apCommMain;
    
    @FXML
    private Label lbPicName;

    @FXML
    private TextField tfAuth;
    
    @FXML
    private TextArea taComm;
    
    @FXML
    private Label lbCommErr;

    /**
     * handle event insert comment
     * if comment is valid, adds comment to the database and 
     * references the to the commented picture
     * 
     * @param event     the click event
     */
    @FXML
    void handleAddComment(ActionEvent event) {
        String auth = tfAuth.getText();
        String comm = taComm.getText();
        if(comm.equalsIgnoreCase("") || auth.equalsIgnoreCase("")){ // check if the comment is valid
            lbCommErr.setText("Insert an anuthor and a comment!");
        } else {            
            commented = true;
            SQLConnection conn = SQLConnection.getConnection(); 
            conn.commentAdd(id, auth, comm);        // adds comments to the database
            closeWindow();
        }
    }

    /**
     * handle event interrupt insert comment
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    void handleCancelComment(ActionEvent event) {
        commented = false;
        closeWindow();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        tfAuth.setOnKeyTyped(e -> {
            if(tfAuth.getLength() >= 128){
                e.consume();

                String n = tfAuth.getText().substring(0, 128);
                tfAuth.setText(n);                    
                tfAuth.positionCaret(tfAuth.getText().length());
            }
        });
    }
    
    /**
     * register which picture is being commented
     * 
     * @param id    the picture's id
     */
    public void setComment(int id){
        SQLConnection conn = SQLConnection.getConnection();
        lbPicName.setText(conn.getPicture(id).getName());
        
        this.id = id;
    }
    
    /**
     * keeps track of whether the comment has been committed
     * 
     * @return      true - commented; false - not commented
     */
    public boolean isCommented(){
        return commented;
    }

    /**
     * register which picture is being commented
     * 
     * @param id    the picture's id
     * @param name  the picture's name
     */
    public void setPicture(int id, String name){
        this.id = id;
        lbPicName.setText(name);
    }
    
    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apCommMain.getScene().getWindow()).close();
    }
    
}
