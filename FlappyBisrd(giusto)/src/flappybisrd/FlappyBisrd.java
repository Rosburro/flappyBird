/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package flappybisrd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Roberto
 */
public class FlappyBisrd extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        //Parent root2 = FXMLLoader.load(getClass().getResource("NewScene.fxml")); 
        
        Scene scene = new Scene(root);
        //Scene scene2 = new Scene(root2);
        
        Image iconaApp = new Image("uccellino.png");
        stage.getIcons().add(iconaApp);
        
        
        stage.setScene(scene);
        //stage.setScene(scene2);
        
        
        stage.setResizable(false);
        stage.setTitle("Flappy Bird");
        stage.requestFocus();
        stage.show();
        stage.setWidth(707.0);
        stage.centerOnScreen();
        
        stage.setHeight(519.0);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
