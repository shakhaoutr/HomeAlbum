/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.picture;

import homealbum.SQLConnection;
import homealbum.PictureManipulator;
import homealbum.Utilities;
import model.Picture;
import model.Settings;

import view.gui.ConfirmWindow;

import java.io.File;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class for GUI picture insert
 *
 * @author Shakhaout Rahman
 */
public class PictureInsertController implements Initializable {
    
    private File selectedFile = null;
    private File selectedDirectory = null;
    
    private int id = -1;
    
    @FXML
    private BorderPane bpMain;

    @FXML
    private MenuBar mbTop;

    @FXML
    private VBox vbPicDetails;

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
    private AnchorPane apImage;

    @FXML
    private ImageView ivPicImg;

    @FXML
    private Button btnOpenImage;

    @FXML
    private TextArea taErrMsg;

    /**
     * opens the directory from which to save the picture
     * @param event     the click event
     */
    @FXML
    void handleSelectDirectory(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(lbDir.getText()));
        selectedDirectory = dc.showDialog(Utilities.getParentStage(bpMain));
        if(selectedDirectory!=null){
            lbDir.setText(selectedDirectory.getAbsolutePath()+"\\");
        }
    }

    /**
     * handles single picture save
     * @param event     the click event
     */
    @FXML
    void handlePictureSave(ActionEvent event) {
        String errmsg = "";
        String name = tfName.getText();
        String ext = lbExt.getText().toLowerCase();
        String place = tfPlace.getText();
        LocalDate date = dpDate.getValue();
        String cat = cbCat.getValue();
        
        /* checks that the required information are not empty */
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
        if(selectedFile == null && ivPicImg.getImage() == null){
            errmsg += "Please select picture\n";
        }
        
        if(errmsg.equalsIgnoreCase("")){
            SQLConnection conn = SQLConnection.getConnection();
            String path = lbDir.getText()+name+ext;             // destination path
            if(name.length() > 120){
                name = name.substring(0, 120);
            }
            if(conn.getPictureId(path) == -1){              // if the picture doesn't exist
                saveAsNewImage(name, ext, place, date, cat);// save as new
            } else {                                        // if exist
                boolean override = ConfirmWindow.display("Override",    // ask to override picture
                        "Picture with this path already exist.\n"
                        + "Do you wish to override it?");
                if(override){
                    overrideImage(name, ext, place, date, cat);         // overrides the picture
                }
            }
        } else {
            taErrMsg.setText(errmsg);
        }
        
    }

    /**
     * selects the picture with file chooser
     * @param event     the click event
     */
    @FXML
    void handleOpenImage(ActionEvent event) {
        if(ivPicImg.getImage() != null){
            ivPicImg.setImage(null);
            selectedFile = null;
            tfName.setText("");
            lbExt.setText("");
            btnOpenImage.setText("Open Image");
        } else {
            FileChooser fc = new FileChooser();            
            fc.getExtensionFilters().addAll(        // allowed extensions of the images
                    new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"),
                    new FileChooser.ExtensionFilter("JPG Image", "*.jpg"), 
                    new FileChooser.ExtensionFilter("JPEG Image", "*.jpeg"), 
                    new FileChooser.ExtensionFilter("PNG Image", "*.png"));
            selectedFile = fc.showOpenDialog(Utilities.getParentStage(bpMain));
            if(selectedFile != null){
                Image image = new Image(selectedFile.toURI().toString());
                ivPicImg.setImage(image);
                tfName.setText(Utilities.getFileName(selectedFile));
                lbExt.setText(Utilities.getFileExt(selectedFile).toLowerCase());
                btnOpenImage.setText("Clear Image");
            }
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadChoiceBoxCats();
        Settings s = Settings.getSettings();
        lbDir.setText(s.BASE_DIR_PIC);
        
        btnSelectDirectory.setDisable(true);
        btnSelectDirectory.setVisible(false);
        
        /* image view properties */
        ivPicImg.setPreserveRatio(true);
        ivPicImg.setManaged(false);
        ivPicImg.fitWidthProperty().bind(
                apImage.widthProperty());       // resized the width
        ivPicImg.fitHeightProperty().bind(
                apImage.heightProperty().subtract(50)); // resized the height
    }
    
    /**
     * sets the image to save
     * @param img       the image to save
     */
    public void setImage(Image img){
        btnOpenImage.setDisable(true);
        btnOpenImage.setVisible(false);
        
        ivPicImg.setImage(img);
        lbExt.setText(".jpeg");
    }
    
    /**
     * sets image to save
     * @param id        the id of the image
     * @param img       the image to save
     */
    public void setImage(int id, Image img){
        btnOpenImage.setDisable(true);
        btnOpenImage.setVisible(false);
        
        this.id = id;
        ivPicImg.setImage(img);
        
        /* load data of the image from database */
        SQLConnection conn = SQLConnection.getConnection();
        Picture p = conn.getPicture(id);
        selectedFile = new File(p.getPath());
        tfName.setText(Utilities.getFileName(selectedFile));
        lbExt.setText(Utilities.getFileExt(selectedFile).toLowerCase());
        tfPlace.setText(p.getPlace());
        dpDate.setValue(p.getDate().toLocalDate());
        if(p.getCategory() == null){
            cbCat.setValue("None");
        } else {
            cbCat.setValue(p.getCategory().getCategory());
        }
                
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
    private void overrideImage(String name, String ext, String place, LocalDate date, String cat){
        boolean overridden = false;
        Settings s = Settings.getSettings();
        
        SQLConnection conn = SQLConnection.getConnection();
        String path = lbDir.getText()+name+ext;             // destination path
        
        PictureManipulator.savePreview(name+ext, ivPicImg.getImage(),   // saves a preview of the picture
                lbExt.getText().replace(".", ""));                      // in the file system
        byte[] preview = PictureManipulator.imageToByteArray(
                s.BASE_DIR_PREVIEW+name+ext);                           // reads the preview as a byte array
        
        if(cat.equalsIgnoreCase("none")){                       
            overridden = conn.pictureModify(id, name+ext,               // inserts the picture in database
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, null, path, preview);
        } else {
            overridden = conn.pictureModify(id, name+ext, 
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, cat, path, preview);
        }
        if(overridden){
            PictureManipulator.saveImageToFile(path,                    // if override is successful
                    ivPicImg.getImage(),                                // saves the new picture
                    lbExt.getText().replace(".", ""));
            File img = new File(path);
            conn.pictureSizeModify(id, (int)(img.length()/1024));       // edit the size
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
        boolean inserted = false;
        Settings s = Settings.getSettings();
        
        SQLConnection conn = SQLConnection.getConnection();
        String path = lbDir.getText()+name+ext;             // destination path
        PictureManipulator.savePreview(name+ext,            // saves a preview of the picture
                ivPicImg.getImage(),                        // in the file system
                lbExt.getText().replace(".", ""));
        byte[] preview = PictureManipulator.imageToByteArray(   // reads the preview as a byte array
                s.BASE_DIR_PREVIEW+name+ext);
        
        if(cat.equalsIgnoreCase("none")){                       // inserts the picture in database
            inserted = conn.pictureInsert(name+ext, 
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, path, preview);
        } else {
            inserted = conn.pictureInsert(name+ext, 
                    Utilities.getSQLDate(date.toString()), 
                    place, 0, cat, path, preview);
        }
        if(inserted){
            PictureManipulator.saveImageToFile(path,            // if insertion is successful
                    ivPicImg.getImage(),                        // saves the new picture
                    lbExt.getText().replace(".", ""));          
            File img = new File(path);
            int picId = conn.getPictureId(path);
            conn.pictureSizeModify(picId, (int)(img.length()/1024));
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
    
}
