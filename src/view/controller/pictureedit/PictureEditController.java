/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.pictureedit;

import homealbum.SQLConnection;
import homealbum.PictureManipulator;
import homealbum.Utilities;

import model.GreyscaleMethod;
import view.controller.picture.PictureInsertController;
import view.gui.AlertWindow;
import view.gui.ConfirmWindow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author Shakhaout Rahman
 */
public class PictureEditController implements Initializable {
    private String cropType;
    private Rectangle rect;
    private Circle cir;
    
    private double startX;
    private double startY;
    
    private int id, width, height;
    
    private ImageView ivPic;
    
    @FXML
    private SplitPane spMain;

    @FXML
    private MenuBar mbHead;

    @FXML
    private AnchorPane apPic;

    @FXML
    private ImageView ivRotR;

    @FXML
    private ImageView ivRotL;
    
    /**
     * saves the image
     * @param event  click event
     */
    @FXML
    void handleMenuSave(ActionEvent event){
        showStagePictureInsert(ivPic.getImage());
    }
    
    /**
     * shows the resize window
     * @param event  click event
     */
    @FXML
    void handleResizeMenu(ActionEvent event){
        Image img = ivPic.getImage();
        showStagePictureResize(Integer.parseInt(ivPic.getId()), img);
    }
    
    /**
     * crops the image
     * it can be rectangular or circular
     * 
     * @param event  click event
     */
    @FXML
    void handleCropMenu(ActionEvent event) {
        Image img = ivPic.getImage();
        Circle c = getScaledCircle();
        if( c!= null){                  // makes a circular crop as selection is circle
            Rectangle r = getRectangleContatingCircle(c);
            
            BufferedImage bfImg = SwingFXUtils.fromFXImage(img, null);
            BufferedImage rzImg;
            rzImg = PictureManipulator.cropImage(bfImg, c);     // crops circularly
            rzImg = PictureManipulator.cropImage(rzImg,         // crops rectangularly to around the circle
                (int)r.getX(), (int)r.getY(), 
                (int)r.getWidth(), (int)r.getHeight());
            showStagePictureInsert(SwingFXUtils.toFXImage(rzImg, null));
        } else {
            Rectangle r = getScaledRectangle();
            if(r != null){                                      // crops rectangularly
                BufferedImage bfImg = SwingFXUtils.fromFXImage(img, null);

                BufferedImage rzImg = PictureManipulator.cropImage(bfImg,
                    (int)r.getX(), (int)r.getY(), 
                    (int)r.getWidth(), (int)r.getHeight());
                showStagePictureInsert(SwingFXUtils.toFXImage(rzImg, null));
            } else {
                AlertWindow.display("Error Cropping", 
                    "Please select an area to crop with Mouse LeftClcik and draw a polygon");
            }
        }
    }
    
    /**
     * apply black & white filter
     * @param event     the click event
     */
    @FXML
    void handleFilterBlackAndWhite(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageBlackAndWhite(
                    SwingFXUtils.fromFXImage(ivPic.getImage(), null));
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageBlackAndWhite(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageBlackAndWhite(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - averaging method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSAveraging(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.AVG);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.AVG, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.AVG, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - de-composition max method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSDecMax(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MAX);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MAX, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MAX, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - de-composition min method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSDecMin(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MIN);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MIN, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DECOM_MIN, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - de-saturation method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSDesat(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DESAT);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DESAT, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.DESAT, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    
    /**
     * apply greyscale filter - luma method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSLuma(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.LUMA);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.LUMA, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.LUMA, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - single-channel blue method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSSngChBlue(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_B);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_B, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_B, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - single-channel green method 
     * @param event     the click event
     */
    @FXML
    void handleFilterGSSngChGreen(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_G);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_G, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_G, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply greyscale filter - single-channel red method
     * @param event     the click event
     */
    @FXML
    void handleFilterGSSngChRed(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_R);
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_R, r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageGreyscale(
                SwingFXUtils.fromFXImage(ivPic.getImage(), null), GreyscaleMethod.SNGCHL_R, c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply negative filter
     * @param event     the click event
     */
    @FXML
    void handleFilterNegative(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageNegative(
                    SwingFXUtils.fromFXImage(ivPic.getImage(), null));
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageNegative(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageNegative(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * apply sepia filter
     * @param event     the click event
     */
    @FXML
    void handleFilterSepia(ActionEvent event) {
        Rectangle r = getScaledRectangle();
        Circle c = getScaledCircle();
        BufferedImage bfImg;
        if(r == null && c == null){
            bfImg = PictureManipulator.filterImageSepia(
                    SwingFXUtils.fromFXImage(ivPic.getImage(), null));
        } else {
            if(r != null ){
                bfImg = PictureManipulator.filterImageSepia(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), r);
                rect = null;
            } else /*if (c != null) */{
                bfImg = PictureManipulator.filterImageSepia(
                        SwingFXUtils.fromFXImage(ivPic.getImage(), null), c);
                cir = null;
            }
        }
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }
    
    /**
     * rotates image by 90 degrees to the left
     * @param event     the click event
     */
    @FXML
    void handleRotateToLeft(ActionEvent event) {
        BufferedImage bfImg = PictureManipulator.rotateImage(ivPic.getImage(), -90);
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }

    /**
     * rotates image by 90 degrees to the right
     * @param event     the click event
     */
    @FXML
    void handleRotateToRight(ActionEvent event) {
        BufferedImage bfImg = PictureManipulator.rotateImage(ivPic.getImage(), 90);
        Image img = SwingFXUtils.toFXImage(bfImg, null);
        setImage(img);
    }
    
    /**
     * shows colour adjust window
     * 
     * @param event     the click event
     */
    @FXML
    void handleMenuColorAdjust(ActionEvent event) {
        showStagePictureColorAdjust(ivPic.getImage());
    }
    
    /**
     * restore the initial image, loses all changes
     * @param event 
     */
    @FXML
    void handleRestore(ActionEvent event) {
        boolean revert = ConfirmWindow.display("Revert", 
                "All progress will be lost. Revert the picture to the initial state?");
        if(revert){
            setImage(Integer.parseInt(ivPic.getId()));
            rect = null;
            cir = null;
        }
    }
    
    /**
     * set the shape of the cropper or region filter to circle
     * @param event     click event
     */
    @FXML
    void handleCropTypeCircle(ActionEvent event) {
        if(!cropType.equalsIgnoreCase("cir")){
            cropType = "cir";
            
            apPic.getChildren().remove(rect);
            
            rect = null;
            cir = null;
            
            addCircleEventHandlers(apPic);
        }
    }
    
    /**
     * set the shape of the cropper or region filter to rectangle
     * @param event     click event
     */
    @FXML
    void handleCropTypeRectangle(ActionEvent event) {
        if(!cropType.equalsIgnoreCase("rect")){
            cropType = "rect";
            
            apPic.getChildren().remove(cir);
            
            rect = null;
            cir = null;
            
            addRectangleEventHandlers(apPic);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* load icons */
        ivRotR.setImage(new Image(new File(""
                + "C:\\Users\\User\\Documents\\NetBeansProjects\\"
                + "HomeAlbum\\media\\icon\\rotate_r.png").toURI().toString()));
        ivRotL.setImage(new Image(new File(""
                + "C:\\Users\\User\\Documents\\NetBeansProjects\\"
                + "HomeAlbum\\media\\icon\\rotate_l.png").toURI().toString()));
        
        rect = null;
        cir = null;
        
        cropType = "rect";
        addRectangleEventHandlers(apPic);
    }
    
    /**
     * set the image to view
     * @param id        the id of the image
     * @param img       the image
     */
    public void setImage(int id, Image img){
        ivPic = new ImageView();
        ivPic.setImage(img);
        ivPic.setId(Integer.toString(id));
        ivPic.setPreserveRatio(true);
        ivPic.setManaged(false);
        ivPic.fitWidthProperty().bind(apPic.widthProperty());
        ivPic.fitHeightProperty().bind(apPic.heightProperty());
        apPic.getChildren().clear();
        apPic.getChildren().add(ivPic);
        
        this.id = id;
        this.width = (int)img.getWidth();
        this.height = (int)img.getHeight();
    }
    
    /**
     * set the image to view
     * @param id        the id of the image
    * @param path       the path of the image
     */
    public void setImage(int id, String path){
        setImage(id, new Image(new File(path).toURI().toString()));
    }
    
    /**
     * set the image to view
     * @param id        the id of the image
     */
    public void setImage(int id){
        SQLConnection conn = SQLConnection.getConnection();
        String path = conn.getPicturePath(id);
        setImage(id, new Image(new File(path).toURI().toString()));
    }
    
    /**
     * set the image to view
     * @param img       the image
     */
    public void setImage(Image img){
        setImage(Integer.parseInt(ivPic.getId()), img);
    }
    
    /**
     * re-scales the rectangle cropper to the original dimension of the image  
     * @return      the re-scaled rectangle
     */
    private Rectangle getScaledRectangle(){
        if(rect == null || rect.getWidth() == 0 || rect.getHeight() == 0){
            return null;
        }
        double sX = (rect.getX() * this.width) / getBoundWidth();
        double sy = (rect.getY() * this.height) / getBoundHeight();
        double sw = (rect.getWidth() * this.width) / getBoundWidth();
        double sh = (rect.getHeight()* this.height) / getBoundHeight();
        
        return new Rectangle(sX, sy, sw, sh);
    }
    
    /**
     * re-scales the circle cropper to the original dimension of the image  
     * @return      the re-scaled circle
     */
    private Circle getScaledCircle(){
        if(cir == null || cir.getRadius() == 0){
            return null;
        }
        double sX = (cir.getCenterX() * this.width) / getBoundWidth();
        double sy = (cir.getCenterY() * this.height) / getBoundHeight();
        double rad = (cir.getRadius()* this.width) / getBoundWidth();
        
        return new Circle(sX, sy, rad);
    }
    
    /**
     * get the rectangle containing the circle
     * @return      the re-scaled rectangle
     */
    private Rectangle getRectangleContatingCircle(Circle cir){
        if(cir == null || cir.getRadius() == 0){
            return null;
        }
                    
        double sX = (cir.getCenterX() - cir.getRadius());
        double sy = (cir.getCenterY() - cir.getRadius());
        double side = (cir.getRadius()* 2);

        return new Rectangle(sX, sy, side, side);
    }
    
    /**
     * gets the rectangle width containing the image
     * @return  the width of the image viewer
     */
    private double getBoundWidth(){
        return Math.floor(ivPic.getBoundsInLocal().getWidth());
    }
    
    /**
     * gets the rectangle height containing the image
     * @return  the height of the image viewer
     */
    private double getBoundHeight(){
        return Math.floor(ivPic.getBoundsInLocal().getHeight());
    }
    
    /**
     * show picture resize window
     * @param id        the picture's id
     * @param img       the image to resize
     */
    public void showStagePictureResize(int id, Image img){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/pictureedit/PictureResize.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureResizeController prc = loader.getController();
            prc.setImage(id, img);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(spMain, stage);
            
            stage.setTitle("ResizeImage - Image id "+id);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            showError(ex, "IO Error during show stage resize");
        }
    }
    
    /**
     * show window to adjust colour of the picture
     * @param img       the picture to adjust
     */
    public void showStagePictureColorAdjust(Image img){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/pictureedit/PictureColorAdjust.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureColorAdjustController pcac = loader.getController();
            pcac.setImage(img, (ColorAdjust)(ivPic.getEffect()));
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(spMain, stage);
            
            stage.setTitle("Adjust Color");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            ColorAdjust ca = pcac.getEffects();
            if(ca != null){             // apply the effetc
                ivPic.setEffect(ca);
                
                SnapshotParameters snp = new SnapshotParameters();
                snp.setFill(Color.TRANSPARENT);
                Image image = ivPic.snapshot(snp, null);
                ivPic.setImage(image);
                ivPic.setEffect(null);
            }
            
        } catch (IOException ex) {
            showError(ex, "IO Error during show stage adjust colours");
        }
    }
    
    private void showStagePictureInsert(Image img){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureInsert.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureInsertController pic = loader.getController();
            pic.setImage(this.id, img);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(spMain, stage);
            
            stage.setTitle("Insert Picture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Error during picture save");
        }
    }
    
    /**
     * draws the rectangle over the image
     * 
     * App, H. (2019). How to make a Javafx Image Crop App. 
     * [online] Stack Overflow. 
     * Available at: https://stackoverflow.com/questions/30993681/how-to-make-a-javafx-image-crop-app/30994313 
     * [Accessed 16 Feb. 2019].
     * 
     * @param ap    the stage where to draw the rectangle  
     */
    private void addRectangleEventHandlers(AnchorPane ap){
        ap.setOnMousePressed(event -> {
            
            if( event.isSecondaryButtonDown()){     // if right-button clicked
                apPic.getChildren().remove(rect);   // remove all
                rect = null;
                return;
            }
            
            if(rect == null){                       // if no rectangle is present
                rect = new Rectangle( 0,0,0,0);     // create it
                rect.setStroke(Color.BLUE);
                rect.setStrokeWidth(1);
                rect.setStrokeLineCap(StrokeLineCap.ROUND);
                rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
            }
            
            double limitX = getBoundWidth();
            double limitY = getBoundHeight();
            
            double mouseX, mouseY;
            
            rect.setX(0);
            rect.setY(0);
            rect.setWidth(0);
            rect.setHeight(0);

            apPic.getChildren().remove(rect);

            if(event.getX() < 0 ){              // get mouse X, set to image bounds if it is out
                mouseX = 0;
            } else if(event.getX() > limitX) {  // get mouse X, set to image bounds if it is out
                mouseX = limitX;
            } else {
                mouseX = event.getX();
            }
            
            if(event.getY() < 0 ){              // get mouse Y, set to image bounds if it is out
                mouseY = 0;
            } else if(event.getY() > limitY) {  // get mouse Y, set to image bounds if it is out
                mouseY = limitY;
            } else {
                mouseY = event.getY();
            }
            
            startX = mouseX;
            startY = mouseY;

            rect.setX(startX);      // set rectangle origin x,y to mouse x,y
            rect.setY(startY);
            rect.setWidth(0);
            rect.setHeight(0);

            apPic.getChildren().add(rect);
        });
        
        /* mouse drag event */
        ap.setOnMouseDragged(event -> {
            if( event.isSecondaryButtonDown()){
                apPic.getChildren().remove(rect);
                rect = null;
                return;
            }

            double limitX = getBoundWidth();
            double limitY = getBoundHeight();
            
            double mouseX, mouseY;
            
            // get current mouse position
            if(event.getX() < 0 ){
                mouseX = 0;
            } else if(event.getX() > limitX) {
                mouseX = limitX;
            } else {
                mouseX = event.getX();
            }
            
            if(event.getY() < 0 ){
                mouseY = 0;
            } else if(event.getY() > limitY) {
                mouseY = limitY;
            } else {
                mouseY = event.getY();
            }
            
            double offsetX = mouseX - startX;
            double offsetY = mouseY - startY;
            
            // draw reverse rectangle if origin is after the current mouse position
            if( offsetX > 0)
                rect.setWidth( offsetX);
            else {
                rect.setX(mouseX);
                rect.setWidth(startX - rect.getX());
            }

            if( offsetY > 0) {
                rect.setHeight( offsetY);
            } else {
                rect.setY(mouseY);
                rect.setHeight(startY - rect.getY());
            }
        });
    }
    
    /**
     * draws circle over image
     * @param ap    the window where to draw image
     */
    private void addCircleEventHandlers(AnchorPane ap){
        ap.setOnMousePressed(event -> {
            if( event.isSecondaryButtonDown()){
                apPic.getChildren().remove(cir);
                cir = null;
                return;
            }
            
            if(cir == null){
                cir = new Circle();
                cir.setStroke(Color.BLUE);
                cir.setStrokeWidth(1);
                cir.setStrokeLineCap(StrokeLineCap.ROUND);
                cir.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
            }
            
            double limitX = getBoundWidth();
            double limitY = getBoundHeight();
            
            double mouseX, mouseY;
            
            cir.setCenterX(0);
            cir.setCenterY(0);
            cir.setRadius(0);
            
            apPic.getChildren().remove(cir);

            if(event.getX() < 0 ){
                mouseX = 0;
            } else if(event.getX() > limitX) {
                mouseX = limitX;
            } else {
                mouseX = event.getX();
            }
            
            if(event.getY() < 0 ){
                mouseY = 0;
            } else if(event.getY() > limitY) {
                mouseY = limitY;
            } else {
                mouseY = event.getY();
            }
            
            startX = mouseX;
            startY = mouseY;

            cir.setCenterX(startX);
            cir.setCenterY(startY);
            cir.setRadius(0);

            apPic.getChildren().add(cir);
        });
        
        ap.setOnMouseDragged(event -> {
            if( event.isSecondaryButtonDown()){
                apPic.getChildren().remove(cir);
                cir = null;
                return;
            }

            double limitX = getBoundWidth();
            double limitY = getBoundHeight();
            
            double mouseX, mouseY;
            
            if(event.getX() < 0 ){
                mouseX = 0;
            } else if(event.getX() > limitX) {
                mouseX = limitX;
            } else {
                mouseX = event.getX();
            }
            
            if(event.getY() < 0 ){
                mouseY = 0;
            } else if(event.getY() > limitY) {
                mouseY = limitY;
            } else {
                mouseY = event.getY();
            }
            
            int dy = (int)Math.abs(cir.getCenterY() - mouseY);
            int dx = (int)Math.abs(cir.getCenterX() - mouseX);
            int dist = (int)Math.hypot(dy, dx);
            
            cir.setRadius(dist);
        });
    }
    
    private void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
    
}
