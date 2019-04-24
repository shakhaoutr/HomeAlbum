/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gui;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *  3.	YouTube. (2019). 
 * JavaFX Java GUI Tutorial - 1 - Creating a Basic Window. 
 * [online] Available at: https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG 
 * 
 * @author User
 */
public class ChoiceBoxWindow {
    
    private static String choice = "";
    
    public static String display(String title, List<String> choices){
        return ChoiceBoxWindow.display(title, choices, "");
    }
    
    public static String display(String title, List<String> choices, String initVal){
        
        
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setWidth(600);
        window.setMinHeight(200);
        window.setHeight(200);
        
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        
        choices.forEach(c -> choiceBox.getItems().add(c));
        
        if(initVal.equalsIgnoreCase("")){
            choiceBox.setValue(choices.get(0));
        } else {
            choiceBox.setValue(initVal);
        }
        
        Button btnYes = new Button("Ok");
        btnYes.setMinWidth(100);
        
        btnYes.setOnAction( e -> {
            choice = choiceBox.getValue();
            window.close();
        });
        
        
        HBox hb = new HBox(20);
        hb.getChildren().addAll(btnYes);
        hb.setAlignment(Pos.CENTER);
        
        VBox vb = new VBox(20);
        vb.getChildren().addAll(choiceBox, hb);
        vb.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
        
        return choice;
    }
}
