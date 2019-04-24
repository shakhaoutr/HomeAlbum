/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.pictureedit;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author Shakhaout Rahman
 */
public class PictureColorAdjustController implements Initializable {

    private double bright, contr, hue, sat; // initial color effects 
    private boolean saved;
    
    @FXML
    private AnchorPane apMain;
    
    @FXML
    private AnchorPane apPic;

    @FXML
    private ImageView ivImg;

    @FXML
    private Slider slBright;

    @FXML
    private Label lbBright;

    @FXML
    private Label lbCon;

    @FXML
    private Label lbHue;

    @FXML
    private Label lbSat;

    @FXML
    private Slider slCon;

    @FXML
    private Slider slHue;

    @FXML
    private Slider slSat;

    /**
     * closes the windows
     * 
     * @param event     click event
     */
    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    /**
     * saves the modified image
     * 
     * @param event     click event
     */
    @FXML
    void handleSave(ActionEvent event) {
        saved = true;
        closeWindow();
    }
    
    /**
     * reset the parameters change
     * 
     * @param event     click event
     */
    @FXML
    void handleReset(ActionEvent event) {
        ColorAdjust ca = new ColorAdjust(hue, sat, bright, contr);
        ivImg.setEffect(ca);
        
        slBright.setValue(bright);
        slCon.setValue(contr);
        slHue.setValue(hue);
        slSat.setValue(sat);
    }
    
    /**
     * event for brightness slider
     * 
     * @param event     mouse scroll event
     */
    @FXML
    void handleSliderScrollBright(ScrollEvent event) {
        handleSlide(event);
    }
    
    /**
     * event for contrast slider
     * 
     * @param event     mouse scroll event
     */
    @FXML
    void handleSliderScrollCon(ScrollEvent event) {
        handleSlide(event);
    }
    
    /**
     * event for hue slider
     * 
     * @param event     mouse scroll event
     */
    @FXML
    void handleSliderScrollHue(ScrollEvent event) {
        handleSlide(event);
    }
    
    /**
     * event for saturation slider
     * 
     * @param event     mouse scroll event
     */
    @FXML
    void handleSliderScrollSat(ScrollEvent event) {
        handleSlide(event);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        saved = false;
    }
    
    /**
     * changes values upon scroll in slider
     * @param event 
     */
    private void handleSlide(ScrollEvent event){
        Slider s = (Slider)event.getSource();
        
        double val = s.getValue();
        
        if(event.getDeltaY() < 0){
            val -= 0.1;
        } else {
            val += 0.1;
        }
        s.setValue(val);
    }
    
    /**
     * set value of the brightness label
     * @param s     the value
     */
    private void setLbBright(String s){
        lbBright.setText(s);
    }
    
    /**
     * set value of the contrast label
     * @param s     the value
     */
    private void setLbCon(String s){
        lbCon.setText(s);
    }
    
    /**
     * set value of the hue label
     * @param s     the value
     */
    private void setLbHue(String s){
        lbHue.setText(s);
    }
    
    /**
     * set value of the saturation label
     * @param s     the value
     */
    private void setLbSat(String s){
        lbSat.setText(s);
    }
    
    /**
     * set the image to adjust the colour
     * @param img   the image
     * @param ca    the previous colour
     */
    public void setImage(Image img, ColorAdjust ca){
        ivImg.setImage(img);
        ivImg.fitWidthProperty().bind(apPic.widthProperty());
        ivImg.fitHeightProperty().bind(apPic.heightProperty());
        
        if(ca == null){
            bright = 0;
            contr = 0;
            hue = 0;
            sat = 0;
            ca = new ColorAdjust(hue, sat, bright, contr);
            ivImg.setEffect(ca);
        } else {
            bright = ca.getBrightness();
            contr = ca.getContrast();
            hue = ca.getHue();
            sat = ca.getSaturation();
            ivImg.setEffect(ca);
        }
        
        setLbBright(Double.toString(ca.getBrightness()));
        setLbCon(Double.toString(ca.getContrast()));
        setLbHue(Double.toString(ca.getHue()));
        setLbSat(Double.toString(ca.getSaturation()));
        
        addSliderListener(slBright, lbBright);
        addSliderListener(slCon, lbCon);
        addSliderListener(slHue, lbHue);
        addSliderListener(slSat, lbSat);
    }
    
    /**
     * apply the effects
     */
    private void updateEffects(){
        double h = Double.parseDouble(lbHue.getText());
        double s = Double.parseDouble(lbSat.getText());
        double b = Double.parseDouble(lbBright.getText());
        double c = Double.parseDouble(lbCon.getText());
        
        ivImg.setEffect(new ColorAdjust(h, s, b, c));
    }
    
    /**
     * value listener, changes the label value upon slider value change
     * @param s
     * @param l 
     */
    private void addSliderListener(Slider s, Label l){
        s.valueProperty().addListener((observable, oldValue, newValue) -> {
            double d = newValue.doubleValue();
            DecimalFormat d1f = new DecimalFormat("#.#");
            
            l.setText(d1f.format(d));
            updateEffects();
        });
        
    }
    
    /**
     * returns the colour effect
     * @return  the colours effect
     */
    public ColorAdjust getEffects(){
        if(saved)
            return (ColorAdjust)ivImg.getEffect();
        else
            return null;
    }

    /**
     * close the window
     */
    private void closeWindow(){
        ((Stage)(apMain.getScene().getWindow())).close();
    }
    
}
