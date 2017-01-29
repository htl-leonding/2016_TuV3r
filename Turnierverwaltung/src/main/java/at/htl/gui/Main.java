package at.htl.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Philipp Krannich on 25.06.2016.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui.fxml"));
        primaryStage.setTitle("Turnier-Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
