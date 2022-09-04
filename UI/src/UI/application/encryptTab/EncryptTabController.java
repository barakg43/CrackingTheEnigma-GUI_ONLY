package UI.applicationGUI.encryptTab;

import UI.applicationGUI.generalComponents.codeFormat.SimpleCode.SimpleCodeController;
import UI.applicationGUI.encryptTab.encryptComponent.EncryptComponentController;
import UI.applicationGUI.encryptTab.statisticsComponent.StatisticsComponentController;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EncryptTabController {

    @FXML public BorderPane mainPaneTab;
    //Current Machine Configuration
    @FXML
    private HBox codeComponent;
    @FXML
    private SimpleCodeController codeComponentController;
    //Statistics Component
    @FXML
    private GridPane statisticsComponent;
    @FXML
    private StatisticsComponentController statisticsComponentController;

    // Encrypt\Decrypt Component
    @FXML
    private VBox encryptComponent;
    @FXML
    private EncryptComponentController encryptComponentController;

    private Engine enigmaEngine;

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
        System.out.println("before binding 1");
//        statisticsComponent.prefHeightProperty().bind(heightProperty);
        mainPaneTab.prefWidthProperty().bind(widthProperty);
        mainPaneTab.prefHeightProperty().bind(heightProperty);
        statisticsComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());
        System.out.println("before binding 2");
//        codeComponent.prefHeightProperty().bind(mainPaneTab.heightProperty());
        codeComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());

        statisticsComponentController.bindSizePropertyToParent(mainPaneTab.widthProperty(),mainPaneTab.heightProperty());
    }


    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEncryptor(enigmaEngine);
    }
    public void doneProcessData()
    {
        statisticsComponentController.updateCodeStatisticsView(enigmaEngine.getStatisticDataDTO());
    }
    @FXML
    private void initialize() {

        encryptComponentController.setParentComponentTab(this);


    }

}
