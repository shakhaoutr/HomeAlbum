/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * 3.	YouTube. (2019). 
 * JavaFX Java GUI Tutorial - 1 - Creating a Basic Window. 
 * [online] Available at: https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG 
 * 
 * @author User
 */
public class AlertWindow {
    public static void display(String title, String msg){
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Alert - "+title);
        window.setMinWidth(200);
        window.setWidth(600);
        window.setMinHeight(200);
        window.setHeight(200);
        
        TextArea taMsg = new TextArea(msg);
        
        taMsg.prefWidthProperty().bind(window.widthProperty().subtract(20));
        taMsg.setWrapText(true);
        taMsg.setEditable(false);
        Button btnClose = new Button("Close");
        btnClose.setMinWidth(100);
        
        btnClose.setOnAction(e -> window.close());
        
        VBox vb = new VBox(20);
        vb.getChildren().clear();
        vb.getChildren().addAll(taMsg, btnClose);
        vb.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
    }
}
