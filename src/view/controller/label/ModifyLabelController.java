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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI edit label
 *
 * @author Shakhaout Rahman
 */
public class ModifyLabelController implements Initializable {

    private boolean changed = false;
    
    @FXML
    private AnchorPane apModLab;

    @FXML
    private Label lbOldLab;

    @FXML
    private TextField tfNewLab;

    @FXML
    private Button btnChgConf;

    @FXML
    private Button btnChgCancel;
    
    @FXML
    private Label lbErrLab;

    /**
     * handle event interrupt edit label
     * closes the window
     * 
     * @param event     the click event
     */
    @FXML
    void handleChangeCancel(ActionEvent event) {
        changed = false;
        closeWindow();
    }

    /**
     * handle event edit label
     * if label is valid, edits label in the database and
     * 
     * @param event     the click event
     */
    @FXML
    void handleChangeConfirm(ActionEvent event) {
        lbErrLab.setText("");
        String old_lab = lbOldLab.getText();
        String new_lab = tfNewLab.getText();
        if(!new_lab.equalsIgnoreCase("")){                      // if new label is not empty
            changed = true;
            if(new_lab.length() > 120){
                new_lab = new_lab.substring(0, 120);
            }
            SQLConnection conn = SQLConnection.getConnection();
            conn.labelModify(old_lab, new_lab);                 // edit change
            closeWindow();
        } else {
            lbErrLab.setText("new label field is empty!");
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
     * keeps track of whether the label has been edited
     * 
     * @return      true - edited; false - not edited
     */
    public boolean isChanged() {
        return changed;
    }
    
    /**
     * set the current value of the label
     * @param lab   the current value
     */
    public void setOldLabel(String lab){
        lbOldLab.setText(lab);
    }
    
    /**
     * close the windows
     */
    private void closeWindow(){
        ((Stage)apModLab.getScene().getWindow()).close();
    }
    
}
