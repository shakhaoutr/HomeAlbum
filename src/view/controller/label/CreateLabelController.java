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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI create label
 *
 * @author Shakhaout Rahman
 */
public class CreateLabelController implements Initializable {

    private boolean created = false;
    
    @FXML
    private TextField tfCreateLabel;
    
    @FXML
    private Label lbErrLab;
    
    /**
     * handle event insert label
     * if comment is valid, adds label to the database and 
     * 
     * @param event     the click event
     */
    @FXML
    private void handleCreateConfirm(ActionEvent event){
        String lab = tfCreateLabel.getText();
        
        if(!(lab.equalsIgnoreCase(""))){
            created = true;
            if(lab.length() > 120){
                lab = lab.substring(0, 120);
            }
            SQLConnection conn = SQLConnection.getConnection();
            conn.labelCreate(lab);
            closeWindow();
        } else {
            lbErrLab.setText("category field is empty!");
        }
        
    }
    
    /**
     * handle event interrupt insert label
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    private void handleCreateCancel(ActionEvent event){
        created = false;
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
     * closes the window
     */
    private void closeWindow(){
        ((Stage)tfCreateLabel.getScene().getWindow()).close();
    }

    /**
     * keeps track of whether the label has been committed
     * 
     * @return      true - inserted; false - not inserted
     */
    boolean isCreated() {
        return created;
    }
    
}
