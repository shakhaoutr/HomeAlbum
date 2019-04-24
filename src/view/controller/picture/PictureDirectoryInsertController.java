/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.picture;

import homealbum.SQLConnection;
import homealbum.PictureManipulator;
import homealbum.Utilities;
import model.Settings;

import view.gui.AlertWindow;
import view.gui.ConfirmWindow;

import java.io.File;
import java.io.FileFilter;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI multiple insert
 *
 * @author Shakhaout Rahman
 */
public class PictureDirectoryInsertController implements Initializable {

    private File selectedDirectory;
    private FileFilter filter;
    
    @FXML
    private BorderPane bpMain;

    @FXML
    private TextArea taErrMsg;

    @FXML
    private Label lbDir;

    @FXML
    private Button btnSelectDirectory;

    @FXML
    private TextField tfName;

    @FXML
    private Label lbExt;

    @FXML
    private TextField tfPlace;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ChoiceBox<String> cbCat;

    @FXML
    private Button btnPicSave;
    
    @FXML
    private Button btnPicSaveSelected;

    @FXML
    private Button btnPicSaveAll;

    @FXML
    private AnchorPane apImage;

    @FXML
    private ListView<String> lvImgs;
    
    @FXML
    private ImageView ivImg;

    /**
     * opens the directory from which to select the pictures
     * @param event     the click event
     */
    @FXML
    void handleSelectDirectory(ActionEvent event) {
        Settings s = Settings.getSettings();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(s.BASE_DIR_PIC));                   // sets the initial directory
        selectedDirectory = dc.showDialog(Utilities.getParentStage(bpMain));
        if(selectedDirectory!=null){
            lbDir.setText(selectedDirectory.getAbsolutePath()+"\\");        // set the path of the chosen directory
            
            lvImgs.getItems().clear();      // clears previus selected directory items
            lbExt.setText(".");
            ivImg.setImage(null);           // clear previous selected image
            
            File[] imgs = selectedDirectory.listFiles();
            for (int i = 0; i < imgs.length; i++) {
                if(imgs[i].isFile()){
                    String fName = imgs[i].getName();
                    if(fName.endsWith(".jpeg")              // adds the image file name
                            || fName.endsWith(".jpg") 
                            || fName.endsWith(".png")){
                        
                        lvImgs.getItems().add(fName);       // to the list   
                    }
                }
            }
            lvImgs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);    // make the list multiple selectable     
        }
    }
    
    /**
     * handles single picture save
     * @param event     the click event
     */
    @FXML
    void handlePictureSave(ActionEvent event) {
        String errmsg = "";
        String name = tfName.getText();             // read the name
        String ext = lbExt.getText().toLowerCase(); // the image's extension
        String place = tfPlace.getText();           // read the place
        LocalDate date = dpDate.getValue();         // read the date
        String cat = cbCat.getValue();              // read the date
        
        if(validateForm(name, place, date, cat)){   // if all required fields are not empry
            Settings settings = Settings.getSettings();
        
            SQLConnection conn = SQLConnection.getConnection();
            String path = settings.BASE_DIR_PIC+name+ext;       // the path for the destination
            int id = conn.getPictureId(path);                   // the the id for a picture with the possible dest path
            if(name.length() > 120){                            // crops the length of the title
                name = name.substring(0, 120);
            }
            if(id == -1){                                       // if picture doesn't exist in database
                saveAsNewImage(name, ext, place, date, cat);    // insert as new picture 
            } else {                                            // picture exist in database
                boolean override = ConfirmWindow.display("Override",    // ask to override picture
                        "Picture with this path already exist.\n"
                        + "Do you wish to override it?");
                if(override){                                           // if affirmative
                    overrideImage(id, name, ext, place, date, cat);     // overrides the picture
                }
            }
        }
    }

    /**
     * handles save for the selected picture
     * @param event     the click event
     */
    @FXML
    void handlePictureSaveSelected(ActionEvent event) {
        if(!lvImgs.getItems().isEmpty()){
            ObservableList<String> names = 
                    lvImgs.getSelectionModel().getSelectedItems();   // gets the name of all selected pictures
            saveMultipleImages(names);      // saves the pictures
        }
        
    }
    
    /**
     * handles save for all pictures in folder
     * @param event 
     */
    @FXML
    void handlePictureSaveAll(ActionEvent event) {
        if(!lvImgs.getItems().isEmpty()){
            ObservableList<String> names = lvImgs.getItems();   // gets the name of all pictures
            saveMultipleImages(names);      // saves the pictures
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadChoiceBoxCats();
        
        addLVSelListener(); // add listener to ListView
        
        ivImg.setPreserveRatio(true);
        ivImg.setManaged(false);
        ivImg.fitWidthProperty().bind(
                apImage.widthProperty().subtract(20));
        ivImg.fitHeightProperty().bind(
                apImage.heightProperty().subtract(300));
        
        dpDate.setValue(LocalDate.now());
        
    }
    
    /**
     * upon click to a name in the list view
     * shows the corresponding image in the image view
     */
    private void addLVSelListener(){
        lvImgs.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {      // selected item change event
                    
            if(!lvImgs.getItems().isEmpty()){       // if list not empty
                if(newVal != null){
                    String path = lbDir.getText()+newVal;
                    Image img = new Image(new File(path).toURI().toString());   // open the image
                    ivImg.setImage(img);                                        // sets the image
                    lbExt.setText(Utilities.getFileExt(path).toLowerCase());    // sets the extension
                    
                } else {
                    lbExt.setText(".");
                    ivImg.setImage(null);       // clears the image
                }
            }
        });
    }
    
    /**
     * checks that the required information are not empty
     * 
     * @param name      the name for the picture
     * @param place     the place for the picture
     * @param date      the date for the picture
     * @param cat       the category for the picture
     * @return          true - valid; false - not valid
     */
    private boolean validateForm(String name, String place, LocalDate date, String cat){
        String errmsg= "";
        
        if(name.equalsIgnoreCase("")){
            errmsg += "Please insert name\n";
        }
        if(place.equalsIgnoreCase("")){
            errmsg += "Please insert place\n";
        }
        if(date == null){
            errmsg += "Please insert date\n";
        }
        if(cat == null){
            errmsg += "Please insert category\n";
        }
        if(selectedDirectory == null && 
                ivImg.getImage() == null){
            
            errmsg += "Please select picture\n";
        }
        if(errmsg.equalsIgnoreCase("")){
            return true;
        } else {
            taErrMsg.setText(errmsg);               // set the error message with the requed field
            return false;
        }
    }
    
    /**
     * saves multiple images
     * @param names     the names of the images to save
     */
    private void saveMultipleImages(ObservableList<String> names){
        ObservableList<String> imgs = names;
            
        imgs.forEach(fileName -> {
            String errmsg = "";
            String name = fileName;
            if(name.length() > 120){                        // trims the names that are too long
                String bname = Utilities.getFileName(name);
                String ext = Utilities.getFileExt(name);
                name = name.substring(0, 120);
                name += ext;
            }
            String place = tfPlace.getText();
            LocalDate date = dpDate.getValue();
            String cat = cbCat.getValue();

            if(validateForm(name, place, date, cat)){       // validates the information 
                Settings settings = Settings.getSettings();

                SQLConnection conn = SQLConnection.getConnection();
                String loadPath = lbDir.getText()+fileName;         // the path of the picture

                Image img = new Image(new File(loadPath).toURI().toString());   // shows the image

                String path = settings.BASE_DIR_PIC+name;       // destination path
                int id = conn.getPictureId(path);               // check if the picture already exist
                if(id == -1){                                       // if nor exist
                    saveAsNewImage(name, place, date, cat, img);    // saves as new picture
                } else {                                            // if exists
                    boolean override = ConfirmWindow.display("Override",    //  ask whether to override
                            "Picture with this path already exist.\n"
                            + "Do you wish to override it?");
                    if(override){                                           // if yes
                        overrideImage(id, name, place, date, cat, img);     // overrides
                    }
                }
            }
        });
    }
    
    /**
     * overrides the image in database
     * @param id        the id of the picture
     * @param name      the base name of the picture
     * @param ext       the extension of the picture
     * @param place     the place of the picture
     * @param date      the date of the picture
     * @param cat       the category of the picture
     */
    private void overrideImage(int id, String name, String ext, String place, LocalDate date, String cat){
        overrideImage(id, name+ext, place, date, cat, ivImg.getImage());
    }
    
    /**
     * overrides the image in database
     * @param id        the id of the picture
     * @param name      the name of the picture
     * @param place     the place of the picture
     * @param date      the date of the picture
     * @param cat       the category of the picture
     */
    private void overrideImage(int id, String name, String place, LocalDate date, String cat, Image image){
        boolean overridden = false;
        Settings s = Settings.getSettings();
        
        SQLConnection conn = SQLConnection.getConnection();
        String path = s.BASE_DIR_PIC+name;                  // destination path
                    
        
        PictureManipulator.savePreview(name, image,             // saves a preview of the picture
                Utilities.getFileExt(name).replace(".", ""));   // in the file system
        byte[] preview = PictureManipulator.imageToByteArray(
                s.BASE_DIR_PREVIEW+name);                       // reads the preview as a byte array
        
        if(cat.equalsIgnoreCase("none")){
            overridden = conn.pictureModify(                        // inserts the picture in database
                    id, name, Utilities.getSQLDate(date.toString()), 
                    place, 0, null, path, preview);
        } else {
            overridden = conn.pictureModify(id, name, 
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, cat, path, preview);
        }
        if(overridden){
            PictureManipulator.saveImageToFile(path, image,         // if override is successful
                    Utilities.getFileExt(name).replace(".", ""));   // saves the new picture
            File img = new File(path);
            conn.pictureSizeModify(id, (int)(img.length()/1024));   // edit the size
            closeWindow();
        }
    }
    
    /**
     * saves the image in database
     * @param id        the id of the picture
     * @param name      the base name of the picture
     * @param ext       the extension of the picture
     * @param place     the place of the picture
     * @param date      the date of the picture
     * @param cat       the category of the picture
     */
    private void saveAsNewImage(String name, String ext, String place, LocalDate date, String cat){
        saveAsNewImage(name+ext, place, date, cat, ivImg.getImage());
    }
    
    /**
     * overrides the image in database
     * @param id        the id of the picture
     * @param name      the name of the picture
     * @param place     the place of the picture
     * @param date      the date of the picture
     * @param cat       the category of the picture
     */
    private void saveAsNewImage(String name, String place, LocalDate date, String cat, Image image){
        boolean inserted = false;
        Settings s = Settings.getSettings();
        
        SQLConnection conn = SQLConnection.getConnection();
        String path = s.BASE_DIR_PIC+name;                  // destination path
                    
        PictureManipulator.savePreview(name, image,             // saves a preview of the picture
                Utilities.getFileExt(name).replace(".", ""));   // in the file system
        
        byte[] preview = PictureManipulator.imageToByteArray(
                s.BASE_DIR_PREVIEW+name);                       // reads the preview as a byte array
        
        if(cat.equalsIgnoreCase("none")){
            inserted = conn.pictureInsert(name,                 // inserts the picture in database
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, path, preview);
        } else {
            inserted = conn.pictureInsert(name, 
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, cat, path, preview);
        }
        if(inserted){
            PictureManipulator.saveImageToFile(path, image,         // if insertion is successful
                    Utilities.getFileExt(name).replace(".", ""));   // saves the new picture
            File img = new File(path);
            int picId = conn.getPictureId(path);
            conn.pictureSizeModify(picId, 
                    (int)(img.length()/1024));                      // edit the size
            closeWindow();
        }            
    }
    
    /**
     * sets the values of the categories in choice box
     */
    private void loadChoiceBoxCats(){
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
        ((Stage)bpMain.getScene().getWindow()).close();
    }
    
    private void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
    
}
