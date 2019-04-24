/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.category;

import homealbum.SQLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for edit category GUI
 *
 * @author Shakhaout Rahman
 */
public class ModifyCategoryController implements Initializable {

    private boolean changed = false;
    
    @FXML
    private AnchorPane apModCat;

    @FXML
    private Label lbOldCat;

    @FXML
    private Label roOldCat1;

    @FXML
    private Label roNewCat;

    @FXML
    private TextField tfNewCat;

    @FXML
    private Button btnChgConf;

    @FXML
    private Button btnChgCancel;
    
    @FXML
    private Label lbErrCat;

    /**
     * handle event edit category interrupt
     * closes windows without doing nothing
     * 
     * @param event     the click event
     */
    @FXML
    void handleChangeCancel(ActionEvent event) {
        changed = false;
        closeWindow();
    }

    /**
     * handle event delete category
     * edits category and saves new value to database
     * 
     * @param event     the click event
     */
    @FXML
    void handleChangeConfirm(ActionEvent event) {
        lbErrCat.setText("");
        String old_cat = lbOldCat.getText();
        String new_cat = tfNewCat.getText();
        if(!new_cat.equalsIgnoreCase("")){      // if new category is valid
            changed = true;
            if(new_cat.length() > 120){
                new_cat = new_cat.substring(0, 120);
            }
            SQLConnection conn = SQLConnection.getConnection();
            conn.categoryModify(old_cat, new_cat);              // saves new value to database
            closeWindow();
        } else {
            lbErrCat.setText("new category field is empty!");   // shows error message
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
     * return whether category was edited or not
     * @return      true - edited; false - not edited
     */
    public boolean isChanged() {
        return changed;
    }
    
    /**
     * sets category to edit
     * @param cat       the category to edit
     */
    public void setOldCategory(String cat){
        lbOldCat.setText(cat);
    }
    
    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apModCat.getScene().getWindow()).close();
    }
    
}
