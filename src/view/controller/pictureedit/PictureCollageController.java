/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.pictureedit;

import homealbum.SQLConnection;
import homealbum.PictureManipulator;
import homealbum.Utilities;

import model.CollageImagePicture;
import model.CollageImageText;
import model.PictureRecord;

import view.controller.picture.PictureInsertController;
import view.gui.AlertWindow;
import view.gui.ChoiceBoxWindow;
import view.gui.ColorPickerWindow;
import view.gui.ConfirmWindow;
import view.gui.TextWindow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * FXML Controller class for GUI collage image
 *
 * @author Shakhaout Rahman
 */
public class PictureCollageController implements Initializable {
    
//    int idTop;
    
    @FXML
    private AnchorPane apMain;
    
    @FXML
    private Pane pnPic;

    @FXML
    private AnchorPane apPic;

    @FXML
    private ScrollPane slPic;

    @FXML
    private HBox hbPic;

    /**
     * scrolls horizontally picture preview bar with mouse
     * 
     * @param event     the scroll event
     */
    @FXML
    void handleMouseScrollMove(ScrollEvent event) {
        double hv = slPic.getHvalue();
        if(event.getDeltaY() < 0){
            hv += 0.1;
        } else {
            hv -= 0.1;
        }
        
        slPic.setHvalue(hv);
    }
    
    /**
     * show window to saves the collage
     * 
     * @param event  the click event
     */
    @FXML
    void handleSaveMenu(ActionEvent event) {
        saveCollage();
    }
    
    /**
     * adds a text to the collage
     * @param event 
     */
    @FXML
    void handleAddText(ActionEvent event) {
        String text = TextWindow.display("Add Text", "");
        
        if(!text.equalsIgnoreCase("")){
            Label l = new Label(text);          // creates the text
            l.setLayoutX(apPic.getWidth()/2);   // sets x,y coordinates
            l.setLayoutY(apPic.getHeight()/2);
            
            l.setFont(new Font("Times New Roman", 24));     // sets font and size
            l.setContentDisplay(ContentDisplay.TOP);        // display content on front
            l.setBackground(new Background(new BackgroundFill(          // transparent background
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
            );
            
            addLabelDragEvent(l);           // adds drag event to text
            addLabelClickEvents(l);         // add clikcs event to text
            addLabelMouseScrollZoom(l);     // add scroll event to text
            addLabelContextMenu(l);         // add context menu (reight-click menu) 
            
            apPic.getChildren().add(l);     // add text in the canvas
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        loadAllImages();        // loads all image in preview bar
    }
    
    /**
     * loads all images in preview bar
     */
    private void loadAllImages(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<PictureRecord> recs;
        recs = conn.getAllPictureRecords();     // read all pictures from db
        
        Collections.sort(recs);
        
        hbPic.getChildren().clear();
        
        hbPic.setMinHeight(160);
        hbPic.setMaxHeight(160);
        hbPic.setSpacing(5);
        hbPic.setPrefWidth(600);

        for (PictureRecord rec : recs) {
            Image img = new Image(new ByteArrayInputStream(rec.getPreview()));
            ImageView iv = new ImageView(img);
            iv.setId(Integer.toString(rec.getPicID()));
            iv.setFitHeight(160);
            iv.setPreserveRatio(true);
            showImageOnClick(iv, rec.getPath());
            hbPic.getChildren().add(iv);            
        }
        
        slPic.setContent(hbPic);
    }
    
    /**
     * adds image in collage canvas when clicked
     * @param iv
     * @param path 
     */
    private void showImageOnClick(ImageView iv, String path){
        iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY){
                    
                    ImageView niv = new ImageView(new Image(new File(path).toURI().toString()));
                    niv.setId(iv.getId());

                    niv.setFitHeight(100);
                    niv.setPreserveRatio(true);
                    niv.setManaged(false);
                    
                    
                    addIVMouseScrollZoom(niv);      // add scroll zoom to image
                    addIVMouseRemove(niv);          // add event remove to image
                    addIVDrag(niv);                 // add image drage to image


                    apPic.getChildren().add(niv);
                    setLabelsOnFront();
                }
                
                event.consume();
            }
        });
    }
    
    /**
     * gets all elements in collage canvas and their coordinates
     */
    private void saveCollage(){
        int width = (int)apPic.getWidth() * 2;          // picture width
        int height = (int)apPic.getHeight() * 2;        // picture height
        
        ObservableList<Node> pics = apPic.getChildren();
        
        ArrayList<CollageImagePicture> imgs = new ArrayList<>();
        ArrayList<CollageImageText> texts = new ArrayList<>();
        pics.forEach(n -> {
            if(n instanceof ImageView){         // gets list and coordinates of all images in collage canvas
                ImageView iv = (ImageView)n;
                imgs.add(getCollageRecord(SwingFXUtils.fromFXImage(iv.getImage(), null), 
                        (int)iv.getLayoutX(), (int)iv.getLayoutY(), 
                        (int)iv.getBoundsInLocal().getWidth(), (int)iv.getBoundsInLocal().getHeight())
                    );
            } else if(n instanceof Label){          // gets list, colours, font, size
                Label l = (Label)n;                 // background and coord of all text in
                Bounds lb = l.getBoundsInLocal();   // collage canvas 
                Color c = (Color)l.getTextFill();
                java.awt.Color color = new java.awt.Color((float)c.getRed(),            // converts FX Colour to AWT Colour 
                        (float)c.getGreen(), (float)c.getBlue(), (float)c.getOpacity());
                
                c = (Color)l.getBackground().getFills().get(0).getFill();
                java.awt.Color bgcolor = new java.awt.Color((float)c.getRed(), 
                        (float)c.getGreen(), (float)c.getBlue(), (float)c.getOpacity());
                
                CollageImageText ct = new CollageImageText(l.getText(),
                        l.getFont().getFamily(),
                        color,
                        (int)(l.getFont().getSize()*2),
                        bgcolor,
                        (int)l.getLayoutX()*2, (int)l.getLayoutY()*2,
                        (int)lb.getWidth()*2, (int)lb.getHeight()*2);
                texts.add(ct);
            }
        });
        if(imgs.isEmpty()){
            AlertWindow.display("No Images", "Plese select images to collage!");
            return;
        }
        BufferedImage clImg = PictureManipulator.collageImage(imgs, texts, width, height,   // create collage
                java.awt.Color.WHITE);
        showStagePictureInsert(SwingFXUtils.toFXImage(clImg, null));            // save collage
    }
    
    /**
     * gets collage image from picture object
     * @param img       the image
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param bw        the width
     * @param bh        the height
     * @return          the collage image record object
     */
    private CollageImagePicture getCollageRecord(BufferedImage img, int x, int y, int bw, int bh){
        CollageImagePicture ci = new CollageImagePicture(img, x*2, y*2, bw*2, bh*2);

        return ci;
    }
    
    /**
     * sets all texts on front
     * so that they aren't covered by the pictures 
     */
    private void setLabelsOnFront(){
        ObservableList<Node> l = apPic.getChildren();
        List<Node> nodesToFront = new ArrayList<>();
        l.forEach(n -> {
            if(n instanceof Label){
                nodesToFront.add(n);
            }
        });
        
        nodesToFront.forEach(n -> {
            n.toFront();
        });
    }
    
    /**
     * list of possible sized for font starting from 8
     * 
     * @param max       the highest value
     * @param leap      the interval between each size eg. 8, 12, 16, 20...,max
     * @return          the list of sizes
     */
    private List<String> getSizes(int max, int leap){
        ArrayList<String> sizes = new ArrayList<>();
        for (int i = 8; i < max; i += leap) {
            sizes.add(Integer.toString(i));
        }
        
        return sizes;
    }
    
    /**
     * adds context menu to label
     * @param l     the label
     */
    private void addLabelContextMenu(Label l){
        ContextMenu lbCM = new ContextMenu();
        
        /* the options in the context menu */
        MenuItem font = new MenuItem("Font");
        MenuItem size = new MenuItem("Size");
        MenuItem color = new MenuItem("Color");
        MenuItem background = new MenuItem("Background");
        MenuItem bguncolor = new MenuItem("Bg-Uncolor");
        MenuItem delete = new MenuItem("Delete");
        lbCM.getItems().addAll(font, size, color, background, bguncolor, delete);
        
        /* event for when the font menu is clicked, allows to change font  */
        font.setOnAction(e -> {
            String f = l.getFont().getFamily();
            String choice = ChoiceBoxWindow.display("Font", Font.getFamilies(), f);
            if(choice != null){
                int s = (int)l.getFont().getSize();
                l.setFont(new Font(choice, s));
            }
            
        });
        
        /* event for when the color menu is clicked, allows to change font color  */
        color.setOnAction(e -> {
            Color c = ColorPickerWindow.display("Color");
            if(c != null){
                l.setTextFill(c);
            }
        });
        
        /* event for when the size menu is clicked, allows to change font size */
        size.setOnAction(e -> {
            String s = ChoiceBoxWindow.display("Size", 
                    (List<String>)getSizes(52, 4), 
                    Integer.toString((int)l.getFont().getSize()));
            if(s != null){
                if(!s.equalsIgnoreCase("")){
                    l.setFont(new Font(l.getFont().getFamily(), Integer.parseInt(s)));
                }
            }
        });
        
        /* event for when the background menu is clicked, allows to change background color  */
        background.setOnAction(e -> {
            Color c = ColorPickerWindow.display("Background Color");
            if(c != null){
                l.setBackground(new Background(new BackgroundFill(
                        c, CornerRadii.EMPTY, Insets.EMPTY))
                );
            }
        });
        
        /* event for when the background uncolor menu is clicked, allows to change background to transparent */
        bguncolor.setOnAction(e -> {
            l.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
            );
        });
        
        /* event for when the delete menu is clicked, allows to delete text */
        delete.setOnAction(e -> { 
            if(ConfirmWindow.display("Delete Text", "Are you sure you want to delete it?")){
                apPic.getChildren().remove(l);
            }
        });
        
        l.setContextMenu(lbCM);
    }
    
    /**
     * add event change size text with mouse scroll
     * @param l     the text
     */
    private void addLabelMouseScrollZoom(Label l){
        l.setOnScroll((ScrollEvent e) -> {
            if(e.getDeltaY() < 0){
                int s = (int)l.getFont().getSize();
                s -= 4;
                s = (s < 8) ? 8 : s;
                
                l.setFont(new Font(l.getFont().getFamily(), s));
            } else {
                int s = (int)l.getFont().getSize();
                s += 4;
                s = (s > 52) ? 52 : s;
                
                l.setFont(new Font(l.getFont().getFamily(), s));
            }
            e.consume();
        });
    }
    
    /**
     * add event edit text with mouse double left-click
     * @param l     the text to edit
     */
    private void addLabelClickEvents(Label l){
        l.setOnMouseClicked(e -> {
            l.setContentDisplay(ContentDisplay.TOP);
            if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2){
                String msg = TextWindow.display("Edit Text", l.getText());
                if(msg.equalsIgnoreCase("")){
                    msg = "------";
                }
                l.setText(msg);
            }
        });
    }
    
    /**
     * add event text drag with mouse click+drag
     * @param l     the text to edit
     */
    private void addLabelDragEvent(Label l){
        l.setOnMouseDragged(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                
                int apW = (int)apPic.getWidth();
                int apH = (int)apPic.getHeight();
                
                int lw = (int)l.getBoundsInLocal().getWidth();
                int lh = (int)l.getBoundsInLocal().getHeight();

                int x =(int)(e.getSceneX() - lw/2);
                int y =(int)(e.getSceneY()-25 - lh/2);
                
                
                x = (x < 0) ? 0 : x;
                y = (y < 0) ? 0 : y;

                
                x = (x > (apW-lw)) ? ((int)(apW-lw)) : x;
                y = (y > (apH-lh)) ? ((int)(apH-lh)) : y;
                
                l.setLayoutX(x);
                l.setLayoutY(y);
            }
        });
    }

    /**
     * add event scroll zoom to image
     * @param iv    the image
     */
    private void addIVMouseScrollZoom(ImageView iv){
        iv.setOnScroll((ScrollEvent e) -> {
            int height = (int)iv.getFitHeight();
            iv.toFront();
            
            if(e.getDeltaY() < 0){
                height -= 10;
                height = (height < 100) ? 100 : height;
            } else {
                height += 10;
                height = (height > 400) ? 400 : height;
            }
            
            iv.setFitHeight(height);
            
            setLabelsOnFront();
        });
    }
    
    /**
     * removes image from collage canvas when right-clicked
     * @param iv    the image to remove
     */
    private void addIVMouseRemove(ImageView iv){
        iv.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                apPic.getChildren().remove(iv);
            }           
        });
    }

    /**
     * allows to drag image in canvas with left-click+drag 
     * @param iv 
     */
    private void addIVDrag(ImageView iv){
        iv.setOnMouseDragged(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                
                iv.toFront();
                
                int apW = (int)apPic.getWidth();
                int apH = (int)apPic.getHeight();
                
                int ivW = (int)iv.getBoundsInLocal().getWidth();
                int ivH = (int)iv.getBoundsInLocal().getHeight();

                int x =(int)(e.getSceneX() - ivW/2);
                int y =(int)(e.getSceneY()-25 - ivH/2);
                
                
                x = (x < (0-ivW*0.7)) ? ((int)(0-ivW*0.7)) : x;     // allow image to cut in border but not disappear
                y = (y < (0-ivH*0.7)) ? ((int)(0-ivH*0.7)) : y;

                
                x = (x > (apW-ivW*0.3)) ? ((int)(apW-ivW*0.3)) : x;     // allow image to cut in border but not disappear
                y = (y > (apH-ivH*0.3)) ? ((int)(apH-ivH*0.3)) : y;
                
                
                iv.setLayoutX(x);
                iv.setLayoutY(y);
            }
            setLabelsOnFront();
        });
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
            pic.setImage(img);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Insert Picture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Exception during show category add stage");
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
