package com.autobackup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargez votre FXML (ici placé sous src/main/resources/fxml/main_view.fxml)
        Parent root = FXMLLoader.load(
            getClass().getResource("/fxml/main_view.fxml")
        );


        Scene scene = new Scene(root);

        scene.getStylesheets().add(
            getClass().getResource("/css/style.css").toExternalForm()
        );
        
        primaryStage.setTitle("AutoBackup");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.ico")));
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
