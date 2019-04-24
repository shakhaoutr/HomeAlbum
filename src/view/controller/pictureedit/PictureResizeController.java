/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.pictureedit;

import homealbum.FXMLDocumentController;
import homealbum.PictureManipulator;
import homealbum.Utilities;
import view.controller.picture.PictureInsertController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import view.gui.AlertWindow;

/**
 * FXML Controller class for GUI resize image
 *
 * @author Shakhaout Rahman
 */
public class PictureResizeController implements Initializable {

    private int id, width, height;  // the id, width, heght of the image
    
    @FXML
    private AnchorPane apMain;

    @FXML
    private TextField tfWidth;

    @FXML
    private TextField tfHeight;

    @FXML
    private CheckBox cbRatio;

    @FXML
    private Button btZMin;

    @FXML
    private Button btZPlus;
    
    @FXML
    private ImageView ivZM;

    @FXML
    private ImageView ivZP;

    @FXML
    private Button btReset;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    private ScrollPane spImage;

    /**
     * reset the original sizes of the image
     * 
     * @param event     the click event
     */
    @FXML
    void handelReset(ActionEvent event) {
        tfWidth.setText(Integer.toString(this.width));
        tfHeight.setText(Integer.toString(this.height));
        
        ImageView iv = (ImageView)spImage.getContent();
        iv.setFitWidth(this.width);
        iv.setFitHeight(this.width);
    }

    /**
     * reduces the size of the image by 10%
     * min sized is -500%
     * 
     * @param event     the click event
     */
    @FXML
    void handleButtonZoomM(ActionEvent event) {
        double tmpWidth = Integer.parseInt(tfWidth.getText()) - (this.width*0.1);
        double tmpHeight = Integer.parseInt(tfHeight.getText()) - (this.height*0.1);
        
        /* if the min sizes is lesser than the allowed size, adjust to the min value */
        if(tmpWidth < this.width/5){
            tmpWidth = this.width/5;
        }
        if(tmpHeight < this.height/5){
            tmpHeight = this.height/5;
        }
        
        tfWidth.setText(Integer.toString((int)tmpWidth));
        tfHeight.setText(Integer.toString((int)tmpHeight));
        setImageViewSize((int)tmpWidth, (int)tmpHeight);
    }

    /**
     * increases the size of the image by 10%
     * max sized is +500%
     * 
     * @param event     the click event
     */
    @FXML
    void handleButtonZoomP(ActionEvent event) {
        double tmpWidth = (this.width*0.1) + Integer.parseInt(tfWidth.getText());
        double tmpHeight = (this.height*0.1) + Integer.parseInt(tfHeight.getText());
        
        /* if the max sizes is greater than the allowed size, adjust to the max value */
        if(tmpWidth > this.width*5){
            tmpWidth = this.width*5;
        }
        if(tmpHeight > this.height*5){
            tmpHeight = this.height*5;
        }
        
        tfWidth.setText(Integer.toString((int)tmpWidth));
        tfHeight.setText(Integer.toString((int)tmpHeight));
        setImageViewSize((int)tmpWidth, (int)tmpHeight);
    }

    /**
     * interrupts resized action
     * closes the window
     * 
     * @param event     click event
     */
    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    /**
     * resizes the image and saves it
     * 
     * @param event     click event
     */
    @FXML
    void handleSave(ActionEvent event) {
        Image img = ((ImageView)spImage.getContent()).getImage();
        BufferedImage bfImg = SwingFXUtils.fromFXImage(img, null);      // converts the image
        BufferedImage rzImg = PictureManipulator.resizeImage(bfImg,     // resized the image
                Integer.parseInt(tfWidth.getText()), 
                Integer.parseInt(tfHeight.getText()));
        showStagePictureInsert(SwingFXUtils.toFXImage(rzImg, null));
    }
    
    /**
     * sets the preserve ratio attribute
     * 
     * @param event     click event
     */
    @FXML
    void handlePreserveRatio(ActionEvent event) {
        resizeWidth();
    }
    
    /**
     * resized the width by reading when key enter is pressed
     * 
     * @param event     the click event
     */
    @FXML
    void handleResizeWidth(ActionEvent event) {
        resizeWidth();
    }
    
    /**
     * resized the height by reading when key enter is pressed
     * 
     * @param event     the click event
     */
    @FXML
    void handleResizeHeight(ActionEvent event) {
        resizeHeight();
    }

    /**
     * validates the width input into the width text field
     * 
     * @param event     click event
     */
    @FXML
    void handleTextZoomW(KeyEvent event) {
        tfWidth.setText(validateString(tfWidth.getText()));
        
        tfWidth.positionCaret(tfWidth.getText().length());  // cursor at the end
    }
    
    /**
     * validates the height input into the height text field
     * 
     * @param event     click event
     */
    @FXML
    void handleTextZoomH(KeyEvent event) {
        tfHeight.setText(validateString(tfHeight.getText()));

        tfHeight.positionCaret(tfHeight.getText().length());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* loads the zoom icons */
        ivZM.setImage(new Image(new File(""
                + "C:\\Users\\User\\Documents\\NetBeansProjects\\HomeAlbum\\"
                + "media\\icon\\zoom_m.png").toURI().toString()));
        
        ivZP.setImage(new Image(new File(""
                + "C:\\Users\\User\\Documents\\NetBeansProjects\\HomeAlbum\\"
                + "media\\icon\\Zoom_p.png").toURI().toString()));
        cbRatio.setSelected(true);
        
        cbRatio.selectedProperty().addListener(new ChangeListener<Boolean>() {      // event on ratio checkbox
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                resizeWidth();
            }
        });
    }

    /**
     * load the image to resize
     * @param id        the id of the image
     * @param img       the image
     */
    public void setImage(int id, Image img){
        ImageView ivPic = new ImageView();
        ivPic.setImage(img);
        ivPic.setId(Integer.toString(id));
        ivPic.setPreserveRatio(true);
        ivPic.setManaged(false);
        spImage.setContent(ivPic);
        
        this.id = id;
        this.width = (int)img.getWidth();
        this.height = (int)img.getHeight();
        
        tfWidth.setText(Integer.toString(this.width));
        tfHeight.setText(Integer.toString(this.height));
    }
    
    /**
     * load the image to resize
     * @param id        the id of the image
     * @param path      the path of the image
     * @param width     the width of the image
     * @param height    the height of the image
     */
    public void setImage(int id, String path, int width, int height){
        setImage(id, new Image(new File(path).toURI().toString()));
    }
    
    /**
     * get the proportional height given the width
     * 
     * @param width     the width for the proportion
     * @return          the proportional height
     */
    private int getScaledHeight(int width){
        int h = (width * this.height) / this.width;
        return h;
    }
    
    /**
     * get the proportional width given the height
     * 
     * @param height    the height for the proportion
     * @return          the proportional width
     */
    private int getScaledWidth(int height){
        int w = (height * this.width) / this.height;
        return w;
    }
    
    /**
     * resized the sizes of the image view
     * @param width     the new width
     * @param height    the new height
     */
    private void setImageViewSize(int width, int height){
        ImageView iv = (ImageView)spImage.getContent();
        iv.setPreserveRatio(cbRatio.isSelected());
        iv.setFitWidth(width);
        iv.setFitHeight(height);
    }
    
    /**
     * resized the image starting from the width value
     */
    private void resizeWidth(){
        int tmpWidth = Integer.parseInt(validateString(tfWidth.getText()));
        
        if(tmpWidth > this.width*5){
            tmpWidth = this.width*5;
        } else if(tmpWidth < this.width/5){
            tmpWidth = this.width/5;
        }
        
        int tmpHeight;
        if(cbRatio.isSelected()){
            tmpHeight = getScaledHeight(tmpWidth);
        } else {
            tmpHeight = Integer.parseInt(validateString(tfHeight.getText()));
        }
        
        if(tmpHeight > this.height*5){
            tmpHeight = this.height*5;
        } else if(tmpHeight < this.height/5){
            tmpHeight = this.height/5;
        }
        
        tfWidth.setText(Integer.toString((int)tmpWidth));
        tfHeight.setText(Integer.toString((int)tmpHeight));
        setImageViewSize((int)tmpWidth, (int)tmpHeight);
    }
    
    /**
     * resizes the image starting from the height
     */
    private void resizeHeight(){
        int tmpHeight = Integer.parseInt(validateString(tfHeight.getText()));
        if(tmpHeight > this.height*5){
            tmpHeight = this.height*5;
        } else if(tmpHeight < this.height/5){
            tmpHeight = this.height/5;
        }
        int tmpWidth;
        if(cbRatio.isSelected()){
            tmpWidth = getScaledWidth(tmpHeight);
        } else {
            tmpWidth = Integer.parseInt(validateString(tfWidth.getText()));
        }
        if(tmpWidth > this.width*5){
            tmpWidth = this.width*5;
        } else if(tmpWidth < this.width/5){
            tmpWidth = this.width/5;
        }
        tfWidth.setText(Integer.toString((int)tmpWidth));
        tfHeight.setText(Integer.toString((int)tmpHeight));
        setImageViewSize((int)tmpWidth, (int)tmpHeight);
    }
    
    /**
     * makes sure that the width or height won't exceed 5 digits
     * @param str       the width or height in string
     * @return          the new string for width or height
     */
    private String validateString(String str){
        String s;
        if(str.length() > 5){
            s = str.substring(0, 5);
        } else {
            s = str;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c < 48 || c > 57){
                s = removeChar(i, s);
                i--;
            }
        }
        return s;
    }

    /**
     * removes the char at position i from the string 
     * @param i     the position
     * @param s     the string
     * @return      the new string
     */
    private String removeChar(int i, String s){
        if(i == 0){
            return s.substring(i+1);
        }
        return s.substring(0, i)+s.substring(i+1);
    }
    
    /**
     * close the window
     */
    private void closeWindow(){
        ((Stage)apMain.getScene().getWindow()).close();
    }
    
    /**
     * shows the window to save the picture
     * @param img       the picture to save
     */
    private void showStagePictureInsert(Image img){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureInsert.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureInsertController pic = loader.getController();
            pic.setImage(id, img);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Insert Picture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Error when saving resized picture");
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
