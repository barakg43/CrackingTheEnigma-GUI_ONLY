package UI.application.encryptTab.encryptComponent;

import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

//video: 100189 - FXML Hello World [JAD, JavaFX] | Powered by SpeaCode
public class HelloFxmlMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.setTitle("Hello There in FXML");

        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UI/application/encryptTab/encryptComponent/encryptComponent.fxml")));
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
