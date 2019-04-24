/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 3.	YouTube. (2019). 
 * JavaFX Java GUI Tutorial - 1 - Creating a Basic Window. 
 * [online] Available at: https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG 
 * 
 * @author User
 */
public class TextWindow {
    
    private static String text = "";
    
    public static String display(String title, String msg){
        return display(title, msg, 128);
    }
    
    public static String display(String title, String msg, int limit){
        
        int LIMIT = limit;
        
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setWidth(600);
        window.setMinHeight(200);
        window.setHeight(200);
        
        TextArea ta = new TextArea(msg);
        ta.setWrapText(true);
        ta.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(ta.getLength() >= LIMIT){
                    event.consume();
                    
                    String n = ta.getText().substring(0, LIMIT);
                    ta.setText(n);                    
                    ta.positionCaret(ta.getText().length());
                }
                
            }
        });
        
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        btnYes.setMinWidth(100);
        btnNo.setMinWidth(100);
        
        btnYes.setOnAction( e -> {
            text = ta.getText();
            window.close();
        });
        
        btnNo.setOnAction( e -> {
            text = msg;
            window.close();
        });
        
        HBox hb = new HBox(20);
        hb.getChildren().addAll(btnYes, btnNo);
        hb.setAlignment(Pos.CENTER);
        
        VBox vb = new VBox(20);
        vb.getChildren().addAll(ta, hb);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(5));
        
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
        
        return text;
    }
}
