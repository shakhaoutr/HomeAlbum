/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * 
 * @author User
 */
public class ColorPickerWindow {
    
    private static Color color;
    
    public static Color display(String title){
        
        
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setWidth(600);
        window.setMinHeight(200);
        window.setHeight(200);
        
        ColorPicker colorPicker = new ColorPicker();
        
        colorPicker.setValue(Color.BLACK);
        
        
        Button btnYes = new Button("Ok");
        btnYes.setMinWidth(100);
        
        btnYes.setOnAction( e -> {
            color = colorPicker.getValue();
            window.close();
        });
        
        
        HBox hb = new HBox(20);
        hb.getChildren().addAll(btnYes);
        hb.setAlignment(Pos.CENTER);
        
        VBox vb = new VBox(20);
        vb.getChildren().addAll(colorPicker, hb);
        vb.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
        
        return color;
    }
}
