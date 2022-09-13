package UI;


import UI.application.AllMachineController;
import UI.application.CommonResourcesPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class UIApplication extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle("Enigma Machine");
            FXMLLoader fxmlLoader=new FXMLLoader();
            URL url=getClass().getResource(CommonResourcesPaths.APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            assert url != null;
            Parent root=fxmlLoader.load(url.openStream());

            Scene scene = new Scene(root,1100,980);
            AllMachineController machineController=fxmlLoader.getController();
            machineController.setSceneWidthHeightProperties(scene.widthProperty(),scene.heightProperty());
            primaryStage.setScene(scene);

            machineController.setSceneWidthHeightProperties(scene.widthProperty(),scene.heightProperty());

            primaryStage.show();
        }


}
