/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controller.picturegallery;

import homealbum.SQLConnection;
import homealbum.Utilities;
import model.Annotation;
import model.Picture;
import model.PictureRecord;

import view.controller.comment.ShowCommentController;
import view.controller.picture.PictureInfoEditController;
import view.controller.picture.PictureLabelsController;
import view.controller.pictureedit.PictureEditController;
import view.gui.AlertWindow;
import view.gui.ColorPickerWindow;
import view.gui.ConfirmWindow;
import view.gui.TextWindow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * FXML Controller class for GUI of image gallery
 * gallery layout source : 
 * 
 * 
 *
 * @author Shakhaout Rahman
 */
public class PictureGalleryHorizontalController implements Initializable {

    private boolean showAnnotation;
    private boolean imageExist = false;
    
    private int currPicID;
    private AnchorPane anns;
    
    @FXML
    private BorderPane bpMain;    
    
    @FXML
    private AnchorPane apMain;

    @FXML
    private StackPane spPic;

    @FXML
    private ListView<String> lvCat;

    @FXML
    private ListView<String> lvLab;

    @FXML
    private ScrollPane slPic;

    @FXML
    private ListView<Label> lvDet;
    
    @FXML
    private MenuItem miShowAnn;
    
    /**
     * show window insert picture
     * 
     * @param event     click event
     */
    @FXML
    void handleInsertPicture(ActionEvent event) {
        showStagePictureInsert();
    }
    
    /**
     * show window insert multiple picture
     * 
     * @param event     click event
     */
    @FXML
    void handleInsertMultiplePicture(ActionEvent event) {
        showStagePictureMultipleInsert();
    }
    
    /**
     * show window edit picture information
     * 
     * @param event     click event
     */
    @FXML
    void handleEditInformation(ActionEvent event) {
        ImageView niv = (spPic.getChildren().isEmpty()) ?
                null : (ImageView)spPic.getChildren().get(0);
        if(niv != null){
            showStageImageEditInfo(Integer.parseInt(niv.getId()));
            showInformations(Integer.parseInt(niv.getId()),
                    (int)niv.getImage().getWidth(),
                    (int)niv.getImage().getHeight());
        }
    }
    
    /**
     * show window picture collage
     * 
     * @param event     click event
     */
    @FXML
    void handleCollageMenu(ActionEvent event) {
        showStagePictureCollage();
    }
    
    /**
     * deletes selected picture
     * 
     * @param event 
     */
    @FXML
    void handleDeletePicture(ActionEvent event) {
        if(ConfirmWindow.display("Delete",                      // confirm delete
                "Are you sure you want to delete the photo?")){
            ImageView niv = (spPic.getChildren().isEmpty()) ?       // get selected picture
                    null : (ImageView)spPic.getChildren().get(0);
            if( niv != null){
                int id = Integer.parseInt(niv.getId());

                SQLConnection conn = SQLConnection.getConnection();
                String path = conn.getPicturePath(id);              

                File img = new File(path);

                if(img.delete()){                                   // delete from file system, if successful
                    conn.pictureDelete(id);                         // delete from database       
                    spPic.getChildren().clear();
                    lvDet.getItems().clear();
                    currPicID = -1;
                    
                    String cat = lvCat.getSelectionModel().getSelectedItem();
                    loadImages(cat, getSelectedLabels());           // reload images
                } else {
                    AlertWindow.display("Error", "Error deleting image");
                }
            }
        }
    }
    
    /**
     * show window categories
     * 
     * @param event     click event
     */
    @FXML
    void handleManageCategories(ActionEvent event) {
        showStageCatView();
        loadCats();
    }
    
    /**
     * show window labels
     * 
     * @param event     click event
     */
    @FXML
    void handleManageLabels(ActionEvent event){
        showStageLabView();
        loadLabs();
    }

    /**
     * show window select labels for picture
     * 
     * @param event     click event
     */
    @FXML
    void handleSetLabels(ActionEvent event) {
        ImageView niv = (spPic.getChildren().isEmpty()) ?
                null : (ImageView)spPic.getChildren().get(0);
        if(niv != null){
            showStageLabSet(Integer.parseInt(niv.getId()));
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
            spPic.getChildren().clear();
            lvDet.getItems().clear();
            currPicID = -1;
        }
    }
    
    /**
     * event for filtering by label
     * @param event     click event
     */
    @FXML
    void handleLabelViewClick(MouseEvent event) {
        if(!lvLab.getItems().isEmpty()){
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
            spPic.getChildren().clear();
            lvDet.getItems().clear();
            currPicID = -1;
        }
    }
    
    /**
     * show window comments for picture
     * 
     * @param event     click event
     */
    @FXML
    void handleManageComments(ActionEvent event) {
        showStageCommView();
    }
    
    /**
     * add annotation to picture
     * 
     * @param event     click event
     */
    @FXML
    void handleAddAnnotation(ActionEvent event) {
        if(isPicSelected()){
            Label l = createAnnotation();       // create annotation
            if(l != null){
                registerAnnotaionEvents(l);
                
                l.toFront();
                anns.getChildren().add(l);
            }
        }
    }

    /**
     * toggle show/hide annotations
     * 
     * @param event     click event
     */
    @FXML
    void handleShowAnnaotation(ActionEvent event) {
        if(currPicID != -1){
            showAnnotation = !showAnnotation;
            if(showAnnotation){
                miShowAnn.setText("Hide Annotations");
                anns.setVisible(true);
            } else {
                miShowAnn.setText("Show Annotations");
                anns.setVisible(false);
            }
        }
    }
    
    /**
     * scrolls image preview bar with mouse scroll
     * @param event 
     */
    @FXML
    void handleMouseScroll(ScrollEvent event) {
        double hv = slPic.getHvalue();
        if(event.getDeltaY() < 0){
            hv += 0.1;
        } else {
            hv -= 0.1;
        }
        
        slPic.setHvalue(hv);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvLab.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        currPicID = -1;
        
        anns = new AnchorPane();    // pane to keep annotations
        showAnnotation = false;     // set visibility
        anns.setVisible(showAnnotation);
        
        loadCats();                 // loads categories
        loadLabs();                 // load labels
        
        /* when a catergory is selected, reaload the images based on the current category
         and labels */
        lvCat.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if(!lvCat.getItems().isEmpty()){
                    String cat = lvCat.getSelectionModel().getSelectedItem();
                    loadImages(cat, getSelectedLabels());
                    spPic.getChildren().clear();
                    lvDet.getItems().clear();
                    currPicID = -1;
                }
            }
        } );
        
        loadImages("All", getSelectedLabels()); // loads all images
    }
    
    /**
     * return whether a a picture in the preview bar has been selected 
     * 
     * @return  true - selected; false - not selected
     */
    private boolean isPicSelected(){
        ObservableList<Node> children = spPic.getChildren();
        
        for(Node n : children){
            if(n instanceof ImageView){
                return true;
            }
        }
        return false;
    }
    
    /**
     * gets the selected image view object
     * 
     * @return  the selected image view or null
     */
    private ImageView getImageView(){
        ObservableList<Node> children = spPic.getChildren();
        
        for(Node n : children){
            if(n instanceof ImageView){
                return (ImageView)n;
            }
        }
        return null;
    }
    
    /**
     * read categories from database and show them in list  
     */
    private void loadCats(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> cats = conn.categorySelectAll();
        lvCat.getItems().clear();
        lvCat.getItems().addAll("All", "No Category");
        cats.forEach((c) -> {
            lvCat.getItems().add(c);
        });
        lvCat.getSelectionModel().select(0);
    }
    
    /**
     * read labels from database and show them in list  
     */
    private void loadLabs(){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<String> labs = conn.labelSelectAll();
        lvLab.getItems().clear();
        labs.forEach((l) -> {
            lvLab.getItems().add(l);
        });
    }
    
    /**
     * get list of selected labels
     * @return      list of labels
     */
    private ArrayList<String> getSelectedLabels(){
        ObservableList<String> lbs = lvLab.getSelectionModel().getSelectedItems();
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
     * loads image previews from database based on category and labels selected 
     * @param cat       the category selected
     * @param labs      the labels selected
     */
    private void loadImages(String cat, ArrayList<String> labs){
        SQLConnection conn = SQLConnection.getConnection();
        ArrayList<PictureRecord> recs;
        
        if(labs.isEmpty()){
            if(cat.equalsIgnoreCase("All")){
                recs = conn.getAllPictureRecords();
            } else if (cat.equalsIgnoreCase("No Category")) {
                recs = conn.getPictureRecordsByCatNULL();
            } else {
                recs = conn.getPictureRecordsByCat(cat);
            }
        } else {
            if(cat.equalsIgnoreCase("All")){
                recs = conn.getAllPictureByLabel(labs);
            } else if (cat.equalsIgnoreCase("No Category")) {
                recs = conn.getPictureByCatNULLAndLabel(labs);
            } else {
                recs = conn.getPictureByCatAndLabel(cat, labs);
            }
        }
        
        Collections.sort(recs);
        
        /* the preview bar containing the previews */
        HBox hbPic = new HBox();
        hbPic.setMinHeight(160);
        hbPic.setMaxHeight(160);
        hbPic.setSpacing(5);
        hbPic.setPrefWidth(600);

        for (PictureRecord rec : recs) {    // for each picture in list
            Image img = new Image(new ByteArrayInputStream(rec.getPreview()));
            ImageView iv = new ImageView(img);
            iv.setId(Integer.toString(rec.getPicID()));
            iv.setFitHeight(160);
            iv.setPreserveRatio(true);
            showImageOnClick(iv, rec.getPath());    // add event which selects it when clicked
            hbPic.getChildren().add(iv);            // add to preview bar       
        }
        
        slPic.setContent(hbPic);
    }
    
    /**
     * when image is clicked, it is shown in the bigger panel 
     * @param iv        the image view of the preview
     * @param path      the path of the picture in the FS
     */
    private void showImageOnClick(ImageView iv, String path){
        iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                File f = new File(path);
                ImageView niv;
                if(f.exists()){
                    imageExist = true;
                    niv = new ImageView(new Image(new File(path).toURI().toString()));
                } else {
                    imageExist = false;
                    niv = new ImageView(new Image(new File(     // when the image is not found
                            "media\\icon\\ImageNotFound.png").toURI().toString()));
                }
                currPicID = Integer.parseInt(iv.getId());
                niv.setId(iv.getId());
                niv.fitHeightProperty().bind(spPic.heightProperty().subtract(10));
                niv.fitWidthProperty().bind(spPic.widthProperty().subtract(10));
                niv.setPreserveRatio(true);
                
                spPic.getChildren().clear();
                spPic.getChildren().add(niv);
                
                
                showImageEditView(niv, Integer.parseInt(iv.getId()));   // when big picture double-cliked, show image edit window
                showInformations(Integer.parseInt(iv.getId()),          // loads image's information
                        (int)niv.getImage().getWidth(),
                        (int)niv.getImage().getHeight());
                loadAllAnnotationsOfPic(currPicID, iv);                 // loads annotations of picture
                
                
                /* reposition the annotations when image window is being resized */
                spPic.widthProperty().addListener((obsW, oldValW, newValW) -> {
                    anns.setPrefWidth(spPic.getWidth());
                    loadAllAnnotationsOfPic(currPicID, iv);
                });
                
                spPic.heightProperty().addListener((obs, oldVal, newVal) -> {
                    anns.setPrefHeight(spPic.getHeight());
                    loadAllAnnotationsOfPic(currPicID, iv);
                });
                
                event.consume();
            }
        });
    }
    
    /**
     * show image edit window upon double-click
     * @param iv    the image view being clicked
     * @param id    the id of the picture in the image view
     */
    private void showImageEditView(ImageView iv, int id){
        iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        if(imageExist){
                            showStagePictureEdit(id);
                        }
                        event.consume();
                    }
                }
 
            }
        });
    }
    
    /**
     * loads and show the information of the picture from database
     * @param id        the id of the picture
     * @param width     the width of the picture
     * @param height    the height of the picture
     */
    private void showInformations(int id, int width, int height){
        currPicID = id;
        
        SQLConnection conn = SQLConnection.getConnection();
        Picture p = conn.getPicture(id);
        ArrayList<String> labs = conn.getAllLabelsOfPic(id);
        
        
        lvDet.getItems().clear();
        
        /* style of the texnt */
        Font header = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 20);
        Font subheader = Font.font("System", FontWeight.BOLD, 16);
        
        Label head = new Label("Picture Details");
        head.setFont(header);
        
        Label lName     = new Label("Name");
        lName.setFont(subheader);
        Label lPath     = new Label("Path");
        lPath.setFont(subheader);
        Label lPlace    = new Label("Place");
        lPlace.setFont(subheader);
        Label lDate     = new Label("Date");
        lDate.setFont(subheader);
        Label lCat      = new Label("Categoty");
        lCat.setFont(subheader);
        Label lSize     = new Label("Size (kB)");
        lSize.setFont(subheader);
        Label lWidth    = new Label("Width (px)");
        lWidth.setFont(subheader);
        Label lHeight   = new Label("Height (px)");
        lHeight.setFont(subheader);
        Label lId       = new Label("Id");
        lId.setFont(subheader);
        Label lIns      = new Label("Insertion Date");
        lIns.setFont(subheader);
        Label lLMod     = new Label("Last Modified");
        lLMod.setFont(subheader);
        Label lLabs     = new Label("Labels");
        lLabs.setFont(subheader);
        
        Label lPPath = new Label(p.getPath());
        lPPath.setMaxWidth(150);
        lPPath.setWrapText(true);
        
        /* add all details in the list view */
        lvDet.getItems().add(head);
        lvDet.getItems().addAll(lName,      new Label(p.getName()));
        lvDet.getItems().addAll(lPath,      lPPath);
        lvDet.getItems().addAll(lPlace,     new Label(p.getPlace()));
        lvDet.getItems().addAll(lDate,      new Label(p.getDate().toString()));
        lvDet.getItems().addAll(lCat,       new Label(p.getCategory().getCategory()));
        lvDet.getItems().addAll(lSize,      new Label(Integer.toString(p.getSize())));
        lvDet.getItems().addAll(lWidth,     new Label(Integer.toString((width))));
        lvDet.getItems().addAll(lHeight,    new Label(Integer.toString((height))));
        lvDet.getItems().addAll(lId,        new Label(Integer.toString(p.getId())));
        lvDet.getItems().addAll(lIns,       new Label(p.getInserted().toString()));
        lvDet.getItems().addAll(lLMod,      new Label(p.getLastModified().toString()));
        lvDet.getItems().add(lLabs);
        if(labs.isEmpty()){
            lvDet.getItems().add(new Label("None"));
        } else {
            labs.forEach((l) -> {
                lvDet.getItems().add(new Label(l));
            });
        }
    }
    
    /**
     * loads the picture's annotations
     * @param id    the id of the picture
     * @param iv    the image view
     */
    private void loadAllAnnotationsOfPic(int id, ImageView iv){
        if(id != -1){
            SQLConnection conn = SQLConnection.getConnection();
            ArrayList<Annotation> annotations = conn.getAllAnnotationsOfPicture(id);

            anns.getChildren().clear();
            spPic.getChildren().remove(anns);
            spPic.getChildren().add(anns);

            anns.setPrefWidth(spPic.getWidth());
            anns.setPrefHeight(spPic.getHeight());

            annotations.forEach(a -> {
                Label l = loadAnnotation(a);
                registerAnnotaionEvents(l);     // add events to the annotations

                l.toFront();
                anns.getChildren().add(l);
            });
        }
    }
    
    /**
     * show picture edit window when double-clicked
     * 
     * @param iv    the annotation's image's image view
     */
    private void addAnnotationPanelEventHandler(ImageView iv){
        anns.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        if(imageExist){
                            showStagePictureEdit(Integer.parseInt(iv.getId()));
                        }
                        event.consume();
                    }
                }
            }
        });
    }
    
    /**
     * add event to the annotations
     * @param l     the annotation
     */
    private void registerAnnotaionEvents(Label l){
        addAnnotationDragEvent(l);
        addAnnotationClickEvents(l);
        addAnnotationMouseScrollZoom(l);
    }
    
    /**
     * Created annotation label
     * @param a     the annotation to load
     * @return      the created label
     */
    private Label loadAnnotation(Annotation a){
        Label l = new Label(a.getText());
        l.setTextFill(Color.web(a.getColor()));
        l.setFont(new Font(a.getSize()));
        l.setLayoutX(getConvertedX(a.getX()));
        l.setLayoutY(getConvertedY(a.getY()));
        
        return l;
    }
    
    /**
     * creates annotation from user input
     * 
     * @return      the created annotation label
     */
    private Label createAnnotation(){
        String text = TextWindow.display("Add Annotation", "", 256);
        if(!text.equalsIgnoreCase("")){
            Label l = new Label(text);
            l.setLayoutX(anns.getWidth()/2);
            l.setLayoutY(anns.getHeight()/2);
            
            l.setFont(new Font("Times New Roman", 24));
            l.setTextFill(Color.WHITE);
            l.setContentDisplay(ContentDisplay.TOP);
            l.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
            );
            
            SQLConnection conn = SQLConnection.getConnection();
            boolean successful = conn.annotationAdd(currPicID, text,        // inserts annotation in database 
                    getScaledX(l.getLayoutX()), getScaledY(l.getLayoutY()), 
                    (int)l.getFont().getSize(),
                    ((Color)(l.getTextFill())).toString());
            
            if(successful){
                return l;
            } else {
                return null;
            }
            
        } else {
            return null;
        }
    }
    
    /**
     * zoom annotation with mouse scroll
     * 
     * @param l  annotation
     */
    private void addAnnotationMouseScrollZoom(Label l){
        l.setOnScroll((ScrollEvent e) -> {
            SQLConnection conn = SQLConnection.getConnection();
            int id = conn.annotationGetID(l.getText());
            if(id != -1){
                if(e.getDeltaY() < 0){
                    int s = (int)l.getFont().getSize();
                    s -= 4;
                    s = (s < 8) ? 8 : s;

                    l.setFont(new Font(l.getFont().getFamily(), s));
                    conn.annotationEdit(id, s);
                } else {
                    int s = (int)l.getFont().getSize();
                    s += 4;
                    s = (s > 52) ? 52 : s;

                    l.setFont(new Font(l.getFont().getFamily(), s));
                    conn.annotationEdit(id, s);
                }
            }
            e.consume();
        });
    }
    
    /**
     * edit annotation      -   left    double-click
     * change font-colour   -   middle  double-click
     * delete               -   right   double-click
     * 
     * @param l     the annotation
     */
    private void addAnnotationClickEvents(Label l){
        l.setOnMouseClicked(e -> {
            SQLConnection conn = SQLConnection.getConnection();
            int id = conn.annotationGetID(l.getText());
            if(id != -1){
                if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2){
                    String msg = TextWindow.display("Edit Annotation", l.getText());
                    if(msg.equalsIgnoreCase("")){
                        msg = "-----";
                    }
                    l.setText(msg);
                    conn.annotationEditText(id, msg);
                    
                } else if(e.getButton() == MouseButton.MIDDLE && e.getClickCount() == 2){
                    Color c = ColorPickerWindow.display("Color");
                    if(c != null){
                        l.setTextFill(c);
                        conn.annotationEditColor(id, c.toString());
                    }
                    
                } else if(e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 2){
                    String msg = l.getText();
                    boolean choice = ConfirmWindow.display("Delete annotation",
                        "Are you sure you want to delete :\n\n"
                        + msg);
                    if(choice){
                        anns.getChildren().remove(l);
                        conn.annotationDelete(id);
                    }
                }
            }
        });
    }
    
    /**
     * drags annotation in picture with mouse-click + drag
     * @param l 
     */
    private void addAnnotationDragEvent(Label l){
        l.setOnMouseDragged(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                
                Bounds pic = getImageView().getBoundsInLocal();
               
                double x = getPicX(e.getSceneX(), l.getWidth());
                double y = getPicY(e.getSceneY(), l.getHeight());

                double minX = ((anns.getWidth() - pic.getWidth())/2);
                double minY = ((anns.getHeight()- pic.getHeight())/2);
                
                Rectangle r = new Rectangle(minX, minY,     // the bounds of the picture in the whole scene
                        pic.getWidth()-l.getWidth(), pic.getHeight()-l.getHeight());
                
                if(r.contains(x, y)){
                    l.setLayoutX(x);
                    l.setLayoutY(y);
                }
            }
        });
        l.setOnMouseReleased(e -> {
            SQLConnection conn = SQLConnection.getConnection();
            int id = conn.annotationGetID(l.getText());
            double x = getScaledX(l.getLayoutX());
            double y = getScaledY(l.getLayoutY());
            
            conn.annotationEdit(id, x, y);
        });
    }
    
    /**
     * converts the x-coord in database to x-coord in the image
     * the image has been divined in a 1000 x 1000 grid
     * 
     * @param x     the x-coord in database
     * @return      the x-coord in image
     */
    private double getConvertedX(double x){
        double apW = anns.getPrefWidth();
        return (x * apW / 1000);
    }
    
    /**
     * converts the y-coord in database to y-coord in the image
     * the image has been divined in a 1000 x 1000 grid
     * 
     * @param y     the y-coord in database
     * @return      the y-coord in image
     */
    private double getConvertedY(double y){
        double apH = anns.getPrefHeight();
        return (y * apH / 1000);
    }
    
    /**
     * converts the x-coord in image to a x-coord for the databse
     * the image has been divined in a 1000 x 1000 grid
     * 
     * @param x     the x-coord in image
     * @return      the x-coord in database
     */
    private double getScaledX(double x){
        double apW = anns.getPrefWidth();
        return (x * 1000 / apW);
    }
    
    /**
     * converts the y-coord in image to a y-coord for the databse
     * the image has been divined in a 1000 x 1000 grid
     * 
     * @param x     the y-coord in image
     * @return      the y-coord in database
     */
    private double getScaledY(double y){
        double apH = anns.getPrefHeight();
        return (y * 1000 / apH);
    }
    
    /**
     * converts the x-coord of the whole scene to a x-coord for the picture only
     * need the width of the label so that it will not be placed exactly where the mouse is
     * but the cursor will be at the center
     * 
     * @param x     the x-coord of the scene
     * @param w     the width of the label
     * @return      the new x-coord
     */
    private double getPicX(double x, double w){
        double spX = bpMain.getCenter().getLayoutX();
        
        return x - spX - (w/2);
    }
    
    /**
     * converts the y-coord of the whole scene to a y-coord for the picture only
     * need the height of the label so that it will not be placed exactly where the mouse is
     * but the cursor will be at the center
     * 
     * @param y     the y-coord of the scene
     * @param h     the height of the label
     * @return      the new y-coord
     */
    private double getPicY(double y, double h){
        double spY = bpMain.getCenter().getLayoutY();
        
        return y - spY - (h/2);
    }
    
    /**
     * show window for picture edit (manipulation)
     * 
     * @param picID  the id of the picture
     */
    private void showStagePictureEdit(int picID){
        try {
            SQLConnection conn = SQLConnection.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/pictureedit/PictureEdit.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureEditController pec = loader.getController();
            pec.setImage(picID);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("View Picture - Pic ID "+picID);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage picture edit");
        }
    }
    
    /**
     * show window to insert picture
     */
    private void showStagePictureInsert(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureInsert.fxml"));
            Parent root = (Parent) loader.load();
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Insert Picture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage picture insert");
        }
    }
    
    /**
     * show window to insert multiple pictures
     */
    private void showStagePictureMultipleInsert(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureDirectoryInsert.fxml"));
            Parent root = (Parent) loader.load();
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Insert Multiple Picture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage picture multiple insert");
        }
    }
    
    /**
     * show window to edit picture info
     */
    private void showStageImageEditInfo(int picID){
        try {
            SQLConnection conn = SQLConnection.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureInfoEdit.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureInfoEditController pec = loader.getController();
            Picture p = conn.getPicture(picID);
            pec.loadInformation(p);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("View Information Edit - "+p.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage picture info edit");
        }
    }
    
    /**
     * show stage picture collage
     */
    private void showStagePictureCollage(){
        try {
            SQLConnection conn = SQLConnection.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/pictureedit/PictureCollage.fxml"));
            Parent root = (Parent) loader.load();
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Collage");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            String cat = lvCat.getSelectionModel().getSelectedItem();
            loadImages(cat, getSelectedLabels());
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage picture info edit");
        }
    }
    
    /**
     * show stage view all categories
     */
    private void showStageCatView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/category/ViewCategory.fxml"));
            Parent root = (Parent) loader.load();
            
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("View Categories");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage view categories");
        }
    }
    
    /**
     * show stage view all comments
     */
    private void showStageCommView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/comment/ShowComment.fxml"));
            Parent root = (Parent) loader.load();
            
            ShowCommentController scc = loader.getController();
            scc.setPic(Integer.parseInt(getImageView().getId()));
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(bpMain, stage);
            
            stage.setTitle("Show Comment");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage view comments");
        }
    }
    
    /**
     * show stage view all comments
     */
    private void showStageLabView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/label/ViewLabel.fxml"));
            Parent root = (Parent) loader.load();
            
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("View Labels");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage view labels");
        }
    }
    
    /**
     * show stage set picture's labels
     */
    private void showStageLabSet(int id){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""
                    + "/view/fxml/picture/PictureLabels.fxml"));
            Parent root = (Parent) loader.load();
            
            PictureLabelsController plc = loader.getController();
            plc.setSelections(id);
            
            Stage stage = new Stage();
            
            Utilities.lockOwnerWindow(apMain, stage);
            
            stage.setTitle("Labels");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            showError(ex, "IO Exception in show stage set picture's labels");
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
