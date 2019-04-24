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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class for create category GUI
 *
 * @author User
 */
public class CreateCategoryController implements Initializable {

    private boolean created = false;
    
    @FXML
    private TextField tfCreateCategory;
    
    @FXML
    private Label lbErrCat;
    
    /**
     * handle event create category
     * creates category and saves in database
     * 
     * @param event     the click event
     */
    @FXML
    private void handleCreateConfirm(ActionEvent event){
        String cat = tfCreateCategory.getText();
        
        if(!(cat.equalsIgnoreCase(""))){    // if category is valid
            created = true;
            if(cat.length() > 120){
                cat = cat.substring(0, 120);
            }
            SQLConnection conn = SQLConnection.getConnection();     
            conn.categoryCreate(cat);                           // save in database
            closeWindow();
        } else {                            // else
            lbErrCat.setText("category field is empty!");       // show error message
        }
        
    }
    
    /**
     * handle event create category interrupt
     * closes windows without doing nothing
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
        ((Stage)tfCreateCategory.getScene().getWindow()).close();
    }

    /**
     * return whether category was created or not
     * @return      true - created; false - not created
     */
    boolean isCreated() {
        return created;
    }
    
}
