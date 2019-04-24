/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homealbum;

import view.gui.AlertWindow;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 *
 * @author Shakhaout Rahman
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane apMain;

    @FXML
    private Button btnStart;

    @FXML
    private ImageView ivLogo;

    @FXML
    private Button btnSettings;

    /**
     * event handler for button settings click
     * 
     * @param event     the click event
     */
    @FXML
    void handleSettings(ActionEvent event) {
        showStageSettings();
    }

    /**
     * event handler for button start click
     * 
     * @param event     the click event
     */
    @FXML
    void handleStart(ActionEvent event) {
        showStagePictureHorizontalGallery();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loads logo image
        ivLogo.setImage(new Image(new File("C:\\Users\\User\\Documents\\"
                + "NetBeansProjects\\HomeAlbum\\media\\icon\\"
                + "HomeAlbumLogo.png").toURI().toString()));
        
        if(!Utilities.testSQLConnection()){     // if db connection is not successfully established
            btnStart.setVisible(false);         // hide start button
        } else {                                // else
            btnStart.setVisible(true);          // show it
        }
    }
    
    /**
     * shows the gallery UI
     */
    private void showStagePictureHorizontalGallery(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picturegallery/PictureGalleryHorizontal.fxml"));
            Parent root = (Parent) loader.load(); // initializes the window
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Picture Gallery");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            showError(ex, "Error displaying gallery");
        }
    }
       
    /**
     * show the settings UI window
     */
    private void showStageSettings(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/gui/fxml/Settings.fxml"));
            Parent root = (Parent) loader.load();
            
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));
            stage.showAndWait();    // shows the window and wiats for its closure
                                    // before continuiing
            
            if(Utilities.testSQLConnection()){     // if db connection is successfully established
                btnStart.setVisible(true);         // shows start button
            } else {
                btnStart.setVisible(false);
            }
        } catch (IOException ex) {
            showError(ex, "Error displaying settings");
        }
    }
    
    private void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, cause);
    }
}
