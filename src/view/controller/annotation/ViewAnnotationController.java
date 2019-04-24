/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.annotation;

import homealbum.SQLConnection;
import homealbum.Utilities;
import java.io.IOException;
import model.Annotation;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.gui.AlertWindow;

/**
 * FXML Controller class for showing all annotations
 *
 * @author Shakhaout Rahman
 */
public class ViewAnnotationController implements Initializable {

    @FXML
    private VBox vbShowAnnMain;

    @FXML
    private Label lbPicPath;

    @FXML
    private TableView<Annotation> tbAnnView;

    @FXML
    private TableColumn<Annotation, String> tbColAnn;

    @FXML
    private TableColumn<Annotation, Timestamp> tbColAdd;

    /**
     * shows annotation add window and reloads them if action happened
     * 
     * @param event     the click event
     */
    @FXML
    void handleAddAnnotation(ActionEvent event) {
        if(showStageAddAnnotation()){
            loadTable();
        }
    }

    /**
     * shows annotation delete window and reloads them if action happened
     * 
     * @param event     the click event
     */
    @FXML
    void handleDeleteAnnotation(ActionEvent event) {
        boolean res = false;
        Annotation a = getSelectedAnnotation();
        if(a != null){
            res = showStageDeleteAnnotation(a);
        }
        if(res){
            loadTable();
        }
    }

    /**
     * shows annotation edit window and reloads them if action happened
     * 
     * @param event     the click event
     */
    @FXML
    void handleEditAnnotation(ActionEvent event) {
        boolean res = false;
        Annotation a = getSelectedAnnotation();
        if(a != null){
            res = showStageEditAnnotation(a);
        }
        if(res){
            loadTable();
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
     * load all annotation of the picture into the table
     */
    private void loadTable(){
        ObservableList<Annotation> anns = getListAnns();
        if(anns != null){
            tbColAnn.setCellValueFactory(new PropertyValueFactory<>("text"));
            tbColAdd.setCellValueFactory(new PropertyValueFactory<>("added"));
            tbAnnView.setItems(anns);
        }
    }

    /**
     * sets the picture's path
     * 
     * @param path  the picture's path
     */
    public void setPicPath(String path){
        lbPicPath.setText(path);
        loadTable();
    }
    
    /**
     * gets the selected annotation in table
     * 
     * @return      the selected annotation
     */
    private Annotation getSelectedAnnotation(){
        Annotation a = tbAnnView.getSelectionModel().getSelectedItem();
        if(a != null){
            return a;
        } else {
            return null;
        }
    }
    
    /**
     * reads all annotation of picture from database
     * 
     * return       array list of annotations 
     */
    private ObservableList<Annotation> getListAnns(){
        if(!(lbPicPath.getText().equalsIgnoreCase(""))){
            SQLConnection conn = SQLConnection.getConnection();
            ArrayList<Annotation> a = conn.getAllAnnotationsOfPicture(conn.getPictureId(lbPicPath.getText()));
            ObservableList<Annotation> anns = FXCollections.observableArrayList();
            a.forEach((com) -> {
                anns.add(com);
            });
            return anns;
        }
        return null;
    }
    
    /**
     * shows window for annotation edit
     * 
     * @param a         annotation to edit
     * @return          whether the annotation was edited or not
     */
    private boolean showStageEditAnnotation(Annotation a){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/annotation/EditAnnotation.fxml"));
            Parent root = (Parent) loader.load();
            
            EditAnnotationController eac = loader.getController();      // gets the window cotroller
            eac.setPicpath(lbPicPath.getText());                        // set the picture path
            eac.setAnnotation(a.getAnnID(), a.getText());               // set the annotatins
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(vbShowAnnMain, stage);
            
            stage.setTitle("Edit Annotation");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return eac.isEdited();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show annotatin edit stage");
            return false;
        }
    }
    
    /**
     * shows window for annotation delete
     * 
     * @param a         annotation to delete
     * @return          whether the annotation was deleted or not
     */
    private boolean showStageDeleteAnnotation(Annotation a){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/annotation/DeleteAnnotation.fxml"));
            Parent root = (Parent) loader.load();
            
            DeleteAnnotationController dac = loader.getController();    // gets the window cotroller
            dac.setPicPath(lbPicPath.getText());                        // set the picture path
            dac.setTextAnnotation(a.getAnnID(), a.getText());           // set the annotatins
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(vbShowAnnMain, stage);
            
            stage.setTitle("Delete Annotation");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return dac.isDeleted();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show annotatin delete stage");
            return false;
        }
    }
    
    /**
     * shows window for annotation add
     * 
     * @return          whether the annotation was added or not
     */
    private boolean showStageAddAnnotation(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/annotation/AddAnnotation.fxml"));
            Parent root = (Parent) loader.load();
            
            AddAnnotationController aac = loader.getController();
            aac.setPicture(lbPicPath.getText());                    // set the picture path
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(vbShowAnnMain, stage);
            
            stage.setTitle("Add Annotation");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return aac.isAnnoted();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show annotatin add stage");
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
