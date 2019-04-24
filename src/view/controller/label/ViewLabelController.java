/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.label;

import homealbum.Utilities;

import homealbum.SQLConnection;

import view.gui.AlertWindow;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for viewing all labels
 *
 * @author Shakhaout Rahman
 */
public class ViewLabelController implements Initializable {

    @FXML
    private BorderPane bpMain;

    @FXML
    private ListView<String> lvLab;

    /**
     * start event to delete label, an the reload list if label was deleted
     * 
     * @param event     the click event on delete button
     */
    @FXML
    void handleLabDelete(ActionEvent event) {
        boolean res = true;
        String lab = getSelectedLab();
        if(lab != null){
            res = stageDeleteLabel(lab);
        }
        if(res){
            loadList();
        }
    }

    /**
     * start event to insert label, an the reload list if label was inserted
     * 
     * @param event     the click event on insert button
     */
    @FXML
    void handleLabInsert(ActionEvent event) {
        boolean res = false;
        res = stageCreateLabel();
        if(res){
            loadList();
        }
    }

    /**
     * start event to edit label, an the reload list if label was edited
     * 
     * @param event     the click event on edit button
     */
    @FXML
    void handleLabModify(ActionEvent event) {
        boolean res = false;
        String cat = getSelectedLab();
        if(cat!=null){
            res = stageModifyLabel(cat);
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
     * loads the list of labels
     */
    private void loadList(){
        setListLab();
    }

    /**
     * close the window
     */
    private void closeWindow(){
        ((Stage)bpMain.getScene().getWindow()).close();
    }
    
    /**
     * gets the selected element from the list
     * @return      the selected label
     */
    private String getSelectedLab(){
        String cat = lvLab.getSelectionModel().getSelectedItem();
        if(cat!=null){
            return cat;
        } else {
            return null;
        }
    }
    
    /**
     * populated the list view with labels
     */
    private void setListLab(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> labs = conn.labelSelectAll();     // reads labels from database
        lvLab.getItems().clear();
        labs.forEach((c) -> {
            lvLab.getItems().add(c);
        });
    }
    
    /**
     * show the window to delete label
     * 
     * @param lab       the label to delete
     * @return          true - deleted; false - not deleted
     */
    private boolean stageDeleteLabel(String lab){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/label/DeleteLabel.fxml"));
            Parent root = (Parent) loader.load();
            
            DeleteLabelController dcc = loader.getController();
            dcc.setDeleteLabel(lab);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Delete Label");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return dcc.isDeleted();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show label delete stage");
            return false;
        }
    }
    
    /**
     * show the window to edit label
     * 
     * @param lab       the label to edit
     * @return          true - edited; false - not edited
     */
    private boolean stageModifyLabel(String lab){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/label/ModifyLabel.fxml"));
            Parent root = (Parent) loader.load();
            
            ModifyLabelController mlc = loader.getController();
            mlc.setOldLabel(lab);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Modify Label");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return mlc.isChanged();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category edit stage");
            return false;
        }
    }
    
    /**
     * show the window to insert category
     * 
     * @return          true - inserted; false - not inserted
     */
    private boolean stageCreateLabel(){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/label/CreateLabel.fxml"));
            Parent root = (Parent) loader.load();
            
            CreateLabelController clc = loader.getController();
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Create Label");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return clc.isCreated();
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
