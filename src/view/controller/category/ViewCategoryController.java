/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.category;

import homealbum.Utilities;
import homealbum.SQLConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import view.gui.AlertWindow;

/**
 * FXML Controller class for viewing all categories
 *
 * @author Shakhaout Rahman
 */
public class ViewCategoryController implements Initializable {

    @FXML
    private BorderPane bpMain;

    @FXML
    private Button btnCatIns;

    @FXML
    private Button btnCatModify;

    @FXML
    private Button btnCatDelete;

    @FXML
    private ListView<String> lvCat;

    /**
     * start event to delete category, an the reload list if category was deleted
     * 
     * @param event     the click event on delete button
     */
    @FXML
    void handleCatDelete(ActionEvent event) {
        boolean res = true;
        String cat = getSelectedCat();
        if(cat != null){
            res = stageDeleteCategory(cat);
        }
        if(res){
            loadList();
        }
    }

    /**
     * start event to insert category, an the reload list if category was inserted
     * 
     * @param event     the click event on insert button
     */
    @FXML
    void handleCatInsert(ActionEvent event) {
        boolean res = false;
        res = stageCreateCategory();
        if(res){
            loadList();
        }
    }

    /**
     * start event to edit category, an the reload list if category was edited
     * 
     * @param event     the click event on edit button
     */
    @FXML
    void handleCatModify(ActionEvent event) {
        boolean res = false;
        String cat = getSelectedCat();
        if(cat!=null){
            res = stageModifyCategory(cat);
        }
        if(res){
            loadList();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadList();
    }
    
    /**
     * load all categories
     */
    private void loadList(){
        setListCat();
    }

    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)bpMain.getScene().getWindow()).close();
    }
    
    /**
     * gets the selected element from the list
     * @return      the selected category
     */
    private String getSelectedCat(){
        String cat = lvCat.getSelectionModel().getSelectedItem();
        if(cat!=null){
            return cat;
        } else {
            return null;
        }
    }
    
    /**
     * populated the list view with categories
     */
    private void setListCat(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> cat = conn.categorySelectAll();
        lvCat.getItems().clear();
        cat.forEach((c) -> {
            lvCat.getItems().add(c);
        });
    }
    
    /**
     * show the window to delete category
     * 
     * @param cat       the category to delete
     * @return          true - deleted; false - not deleted
     */
    private boolean stageDeleteCategory(String cat){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/category/DeleteCategory.fxml"));
            Parent root = (Parent) loader.load();
            
            DeleteCategoryController dcc = loader.getController();
            dcc.setDeleteCategory(cat);                             // sets the category to delete
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Delete Category");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return dcc.isDeleted();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category delete stage");
            return false;
        }
    }
    
    /**
     * show the window to edit category
     * 
     * @param cat       the category to edit
     * @return          true - edited; false - not edited
     */
    private boolean stageModifyCategory(String cat){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/category/ModifyCategory.fxml"));
            Parent root = (Parent) loader.load();
            
            ModifyCategoryController mcc = loader.getController();
            mcc.setOldCategory(cat);                                // sets category to edit
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Modify Category");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return mcc.isChanged();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category edit stage");
            return false;
        }
    }
    
    /**
     * show the window to insert category
     * 
     * @param cat       the category to insert
     * @return          true - inserted; false - not inserted
     */
    private boolean stageCreateCategory(){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/category/CreateCategory.fxml"));
            Parent root = (Parent) loader.load();
            
            CreateCategoryController ccc = loader.getController();
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Create Category");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return ccc.isCreated();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category add stage");
            return false;
        }
    }
    
    private void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
}
