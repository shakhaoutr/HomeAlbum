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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for delete category GUI
 *
 * @author Shakhaout Rahman
 */
public class DeleteCategoryController implements Initializable {

    private boolean deleted;
    
    @FXML
    private AnchorPane apCatDel;

    @FXML
    private Label roDelCat;

    @FXML
    private Label lbDelCat;

    @FXML
    private Button btnDeleteConf;

    @FXML
    private Button btnDeleteCancel;

    /**
     * handle event delete category interrupt
     * closes windows without doing nothing
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteCalcel(ActionEvent event) {
        deleted = false;
        closeWindow();
    }

    /**
     * handle event delete category
     * delete category and removes from database
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteConfirm(ActionEvent event) {
        deleted = true;
        SQLConnection conn = SQLConnection.getConnection();
        conn.categoryDelete(lbDelCat.getText());            // delete from database
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
     * return whether category was deleted or not
     * @return      true - deleted; false - not deleted
     */
    public boolean isDeleted(){
        return deleted;
    }
    
    /**
     * sets category to delete
     * @param cat       the category to delete
     */
    public void setDeleteCategory(String cat){
        lbDelCat.setText(cat);
    }

    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apCatDel.getScene().getWindow()).close();
    }
    
}
