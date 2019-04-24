/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.comment;

import homealbum.SQLConnection;
import homealbum.FXMLDocumentController;
import homealbum.Utilities;
import model.Comment;

import java.io.IOException;
import java.sql.Timestamp;
import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import view.gui.AlertWindow;


/**
 * FXML Controller class for GUI view comments of picture
 *
 * @author Shakhaout Rahman
 */
public class ShowCommentController implements Initializable {

    int id;
    
    @FXML
    private BorderPane bpMain;

    @FXML
    private Label lbPicName;
    
    @FXML
    private TableView<Comment> tbCommView;

    @FXML
    private TableColumn<Comment, String> tbColAuth;
    
    @FXML
    private TableColumn<Comment, String> tbColComm;

    @FXML
    private TableColumn<Comment, Timestamp> tbColAdd;

    /**
     * shows the new window to add comment
     * 
     * @param event     the click event on add button
     */
    @FXML
    void handleAddComment(ActionEvent event) {       
        if(showStageAddComment()){      // if an insert has been made, reload comments
            loadTable();
        }
    }

    /**
     * shows the new window to delete comment
     * 
     * @param event     the click event on delete button
     */
    @FXML
    void handleDeleteComment(ActionEvent event) {
        boolean res = false;
        Comment c = getSelectedComment();
        if(c != null){
            res = showStageDeleteComment(c);
        }
        if(res){            // if an deletion has been made, reload comments
            loadTable();
        }
    }

    /**
     * shows the new window to edit comment
     * @param event     the click event on edit button
     */
    @FXML
    void handleEditComment(ActionEvent event) {
        boolean res = false;
        Comment c = getSelectedComment();
        if(c != null){
            res = showStageEditComment(c);
        }
        if(res){            // if an edit has been made, reload comments
            loadTable();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    /**
     * load all comments of picture in table
     */
    private void loadTable(){
        ObservableList<Comment> comms = getListComms();     // gets the comments of picture
        if(!comms.isEmpty()){
            tbColAuth.setCellValueFactory(new PropertyValueFactory<>("author"));    // initializes the author column
            tbColComm.setCellValueFactory(new PropertyValueFactory<>("text"));      // initializes the text column
            tbColAdd.setCellValueFactory(new PropertyValueFactory<>("added"));      // initializes the time added column
            tbCommView.setItems(comms);
        }
    }

    /**
     * set the information of the picture
     * @param id    the id of the picture in database
     */
    public void setPic(int id){
        SQLConnection conn = SQLConnection.getConnection();
        String n = conn.getPictureName(id);
        this.id = id;
        
        lbPicName.setText(n);
        loadTable();
    }
    
    /**
     * gets the current selected comment in table
     * @return  the selected comment or null
     */
    private Comment getSelectedComment(){
        Comment c = tbCommView.getSelectionModel().getSelectedItem();
        if(c != null){
            return c;
        } else {
            return null;
        }
    }
    
    /**
     * gets a list comment of the picture
     * @return 
     */
    private ObservableList<Comment> getListComms(){
        if(!(lbPicName.getText().equalsIgnoreCase(""))){
            SQLConnection conn = SQLConnection.getConnection();
            ArrayList<Comment> c = conn.getCommentByPicID(id);                      // reads the list from database
            ObservableList<Comment> comms = FXCollections.observableArrayList();
            c.forEach((com) -> {            // copies the arraylist to the observable list
                comms.add(com);
            });
            return comms;
        }
        return null;
    }
    
    /**
     * show the window to edit the comment
     * @param c     the selected comment
     * @return      true - edited; false - not edited
     */
    private boolean showStageEditComment(Comment c){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/comment/EditComment.fxml"));
            Parent root = (Parent) loader.load();
            
            EditCommentController ecc = loader.getController();         // controller object of the window 
            ecc.setPicName(lbPicName.getText());                        // set picture
            ecc.setComment(c.getCommID(), c.getAuthor(), c.getText());  // set comment
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Edit Comment");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return ecc.isEdited();                                      // return edit made or not
        } catch (IOException ex) {
            showError(ex, "IO Exception during show comment edit stage");
            return false;
        }
    }
    
    /**
     * show the window to delete the comment
     * @param c     the selected comment
     * @return      true - deleted; false - not deleted
     */
    private boolean showStageDeleteComment(Comment c){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/comment/DeleteComment.fxml"));
            Parent root = (Parent) loader.load();
            
            DeleteCommentController dcc = loader.getController();
            dcc.setPicName(lbPicName.getText());
            dcc.setTextComment(c.getCommID(), c.getText());
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Delete Comment");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return dcc.isDeleted();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show comment delete stage");
            return false;
        }
    }
    
    /**
     * show the window to insert the comment
     * @param c     the selected comment
     * @return      true - inserted; false - not inserted
     */
    private boolean showStageAddComment(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/comment/AddComment.fxml"));
            Parent root = (Parent) loader.load();
            
            AddCommentController acc = loader.getController();
            acc.setPicture(this.id, lbPicName.getText());
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Add Comment");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            return acc.isCommented();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category edit stage");
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
