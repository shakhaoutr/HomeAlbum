/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.picture;

import homealbum.SQLConnection;
import homealbum.Utilities;
import model.Picture;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI picture edit information
 *
 * @author Shakhaout Rahman
 */
public class PictureInfoEditController implements Initializable {

    // current informations
    private int id;
    private String oName;
    private String oPlace;
    private LocalDate oDate;
    private String oCat;
    
    @FXML
    private AnchorPane apMain;
    
    @FXML
    private Label lbExt;
    
    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPlace;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ChoiceBox<String> cbCat;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    /**
     * handle event interrupt picture information change
     * closes windows without doing nothing
     * 
     * @param event     the click event
     */
    @FXML
    void handleCancelButton(ActionEvent event) {
        closeWindow();
    }

    /**
     * saves the changes to picture information
     * 
     * @param event     the click event
     */
    @FXML
    void handleSaveButton(ActionEvent event) {
        SQLConnection conn = SQLConnection.getConnection();
        
        String nName = tfName.getText();
        if(!oName.equals(nName)){                   // if name has changed
            if(nName.length() > 120){
                nName = nName.substring(0, 120);
            }
            conn.pictureNameModify(id, nName+lbExt.getText());      // save change in database
        }
        
        String nPlace = tfPlace.getText();
        if(!oPlace.equals(nPlace)){                 // if place has changed
            conn.picturePlaceModify(id, nPlace);    // save change in database
        }
        
        LocalDate nDate = dpDate.getValue();        
        if(!oDate.equals(nDate)){                   // if date has changed
            conn.pictureDateModify(id,              // save change in database
                Utilities.getSQLDate(nDate.toString()));
        }
        
        String nCat = cbCat.getValue();             
        if(!oCat.equalsIgnoreCase(nCat)){           // if category has changed
            conn.pictureCategoryModify(id, nCat);   // save change in database
        }
        
        closeWindow();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadChoiceBox();
    }

    /**
     * loads name, place, date, category of picture
     * @param p     the picture to load
     */
    public void loadInformation(Picture p){
        id = p.getId();
        
        oName = Utilities.getFileName(p.getName());
        tfName.setText(oName);
        
        lbExt.setText(Utilities.getFileExt(p.getName()));
        
        oPlace = p.getPlace();
        tfPlace.setText(oPlace);
        
        oDate = p.getDate().toLocalDate();
        dpDate.setValue(oDate);
        
        oCat = p.getCategory().getCategory();
        cbCat.setValue(oCat);
    }
    
    /**
     * sets the values of the categories in choice box
     */
    private void loadChoiceBox(){
        cbCat.getItems().addAll(getListCat());
        cbCat.setValue("None");
    }
    
    /**
     * loads all the categories
     * @return      the list of categories
     */
    private ObservableList<String> getListCat(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> cat = conn.categorySelectAll();
        ObservableList<String> cats = FXCollections.observableArrayList();
        cats.add("None");
        cat.forEach((c) -> {
            cats.add(c);
        });
        return cats;
    }
    
    /**
     * close the window
     */
    private void closeWindow(){
        ((Stage)apMain.getScene().getWindow()).close();
    }
}
