/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.picture;

import homealbum.SQLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for GUI picture labels
 * changes label of picture
 *
 * @author Shakhaout Rahman
 */
public class PictureLabelsController implements Initializable {

    private int picture;
    private ArrayList<String> oldLabels;
    
    @FXML
    private AnchorPane apMain;
    
    @FXML
    private ListView<String> lvLabels;

    /**
     * handle event interrupt labels change
     * closes windows without doing nothing
     * 
     * @param event     the click event
     */
    @FXML
    void handleLabeCancel(ActionEvent event) {
        closeWindow();
    }

    /**
     * changes the labels of the picture 
     * 
     * @param event 
     */
    @FXML
    void handleLabelSave(ActionEvent event) {
        SQLConnection conn = SQLConnection.getConnection();
        
        ArrayList<String> del = getLabelsToRemove(oldLabels, getSelectedLabels()); // gets the labels to remove
        del.forEach((l) -> {
            conn.labelpicDelete(l, picture);        // removes them from picture
        }); 
        
        ArrayList<String> add = getLabelsToAdd(oldLabels, getSelectedLabels());     // gets the labels to add
        add.forEach((l) -> {
            conn.labelpicAdd(l, picture);           // adds them from picture
        });
        
        closeWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvLabels.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> labs = conn.labelSelectAll();
        
        lvLabels.getItems().clear();
        labs.forEach((l) -> {           // loads the list of labels
            lvLabels.getItems().add(l);
        });        
    }
    
    /**
     * selects the labels associated to the picture
     * 
     * @param id    id of the picture
     */
    public void setSelections(int id){
        picture = id;
        
        SQLConnection conn = SQLConnection.getConnection();
        oldLabels = conn.getAllLabelsOfPic(id);
        
        ObservableList<String> lbs = lvLabels.getItems();
        lbs.forEach((l) -> {
            if(oldLabels.contains(l)){
                lvLabels.getSelectionModel().select(l);     // if picture has this label, select it
            }
        });
    }
    
    /**
     * gets list of selected label
     * 
     * @return      the list of labels
     */
    private ArrayList<String> getSelectedLabels(){
        ObservableList<String> lbs = lvLabels.getSelectionModel().getSelectedItems();
        if(lbs.isEmpty()){
            return new ArrayList<>(0);
        }
        ArrayList<String> labs = new ArrayList<>(lbs.size());
        lbs.forEach((s) -> {
            labs.add(s);
        });
        labs.trimToSize();
        return labs;
    }
    
    /**
     * gets the labels to remove from the picture
     * 
     * @param old       the list of the previous labels
     * @param news      the list of the newly selected labels
     * @return          the list of the labels to remove from picture
     */
    private ArrayList<String> getLabelsToRemove(ArrayList<String> old, ArrayList<String> news){
        ArrayList<String> del = new ArrayList<>();
        
        old.forEach((l) -> {
            if(!news.contains(l)){  // if the old label isn't in the list of the new labels
                del.add(l);         // add it in the remove list
            }
        });
        del.trimToSize();
        return del;
    }
    
    /**
     * gets the labels to add to the picture
     * 
     * @param old       the list of the previous labels
     * @param news      the list of the newly selected labels
     * @return          the list of the labels to remove from picture
     */
    private ArrayList<String> getLabelsToAdd(ArrayList<String> old, ArrayList<String> news){
        ArrayList<String> add = new ArrayList<>();
        
        news.forEach((l) -> {
            if(!old.contains(l)){   // if the new label isn't in the list of old labels
                add.add(l);         // add it in the add list
            }
        });
        add.trimToSize();
        return add;
    }

    /**
     * closes the window
     */
    private void closeWindow(){
        ((Stage)apMain.getScene().getWindow()).close();
    }
    
}
