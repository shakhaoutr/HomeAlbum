/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class ConfirmWindow {
    
    private static boolean choice = false;
    
    public static boolean display(String title, String msg){
        
        
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirm - "+title);
        window.setMinWidth(200);
        window.setWidth(600);
        window.setMinHeight(200);
        window.setHeight(200);
        
        Label lbMsg = new Label(msg);
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        btnYes.setMinWidth(100);
        btnNo.setMinWidth(100);
        
        btnYes.setOnAction( e -> {
            choice = true;
            window.close();
        });
        
        btnNo.setOnAction( e -> {
            choice = false;
            window.close();
        });
        
        HBox hb = new HBox(20);
        hb.getChildren().addAll(btnYes, btnNo);
        hb.setAlignment(Pos.CENTER);
        
        VBox vb = new VBox(20);
        vb.getChildren().addAll(lbMsg, hb);
        vb.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
        
        return choice;
    }
}
