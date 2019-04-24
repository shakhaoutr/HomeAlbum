/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.label;

import homealbum.SQLConnection;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI delete label
 *
 * @author Shakhaout Rahman
 */
public class DeleteLabelController implements Initializable {

    private boolean deleted;
    
    @FXML
    private AnchorPane apLabDel;

    @FXML
    private Label lbDelLab;

    @FXML
    private Button btnDeleteConf;

    @FXML
    private Button btnDeleteCancel;

    /**
     * handle event interrupt delete label
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteCalcel(ActionEvent event) {
        deleted = false;
        closeWindow();
    }

    /**
     * handle event delete label
     * if user conforms, the label is deleted from database
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteConfirm(ActionEvent event) {
        deleted = true;
        SQLConnection conn = SQLConnection.getConnection();
        conn.labelDelete(lbDelLab.getText());               // deletion
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
     * keeps track of whether the label has been deleted
     * 
     * @return      true - deleted; false - not deleted
     */
    public boolean isDeleted(){
        return deleted;
    }
    
    /**
     * sets which label to delete
     * @param lab 
     */
    public void setDeleteLabel(String lab){
        lbDelLab.setText(lab);
    }

    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apLabDel.getScene().getWindow()).close();
    }
    
}
