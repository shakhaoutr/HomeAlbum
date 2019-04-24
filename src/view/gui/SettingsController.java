/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import homealbum.Utilities;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Settings;

/**
 * FXML Controller class
 *
 * @author User
 */
public class SettingsController implements Initializable {

    private final String DB_CLASS = "com.mysql.cj.jdbc.Driver";
    private final String DB_DRIVER = "jdbc:mysql:";
    
    @FXML
    private AnchorPane apMain;

    @FXML
    private Label lbSettings;

    @FXML
    private Label lbDir;

    @FXML
    private TextField tfDBName;

    @FXML
    private TextField tfDBUser;

    @FXML
    private TextField tfDBPasswd;

    @FXML
    private TextField tfDBClass;

    @FXML
    private TextField tfDBDriver;

    @FXML
    private TextField tfDBHost;
    
    @FXML
    void handelOpenDir(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        String initD = lbDir.getText();
        if(!initD.equalsIgnoreCase("")){
            dc.setInitialDirectory(new File(initD));
        }
        File selectedDirectory = dc.showDialog(Utilities.getParentStage(apMain));
        if(selectedDirectory!=null){
            lbDir.setText(selectedDirectory.getAbsolutePath()+"\\");
        }
    }
    
    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    void handleReset(ActionEvent event) {
        loadSettings();
    }
    
    @FXML
    void handleRestoreDefault(ActionEvent event) {
        lbDir.setText("");
        tfDBClass.setText(DB_CLASS);
        tfDBDriver.setText(DB_DRIVER);
        tfDBHost.setText("");
        tfDBName.setText("");
        tfDBUser.setText("");
        tfDBPasswd.setText("");
    }

    @FXML
    void handleSave(ActionEvent event) {
        File dPic = new File(lbDir.getText());
        if(!dPic.exists()){
            dPic.mkdirs();
        }
        File dPrev = new File(lbDir.getText()+"preview\\");
        if(!dPrev.exists()){
            dPrev.mkdirs();
        }
        
        saveSettings();
        closeWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSettings();
    }

    private void loadSettings(){
        Settings s = Settings.getSettings();
        
        lbDir.setText(s.BASE_DIR_PIC);
        tfDBClass.setText(s.DB_CLASS);
        tfDBDriver.setText(s.DB_DRIVER);
        tfDBHost.setText(s.DB_HOST);
        tfDBName.setText(s.DB_NAME);
        tfDBUser.setText(s.DB_USER);
        tfDBPasswd.setText(s.DB_PASSWD);
    }
    
    private void saveSettings(){
        String dir = lbDir.getText();
        String cls = tfDBClass.getText();
        String dri = tfDBDriver.getText();
        String host = tfDBHost.getText();
        String name = tfDBName.getText();
        String user = tfDBUser.getText();
        String pass = tfDBPasswd.getText();
        
        Settings s = Settings.getSettings();
        s.saveToFile(dir, cls, dri, host, name, user, pass);
    }
    
    private void closeWindow(){
        ((Stage)apMain.getScene().getWindow()).close();
    }
    
}
