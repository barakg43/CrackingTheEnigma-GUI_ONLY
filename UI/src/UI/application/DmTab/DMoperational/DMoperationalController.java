//package UI.application.DmTab.DMoperational;
//
//import decryptionManager.DecryptionManager;
//import dtoObjects.DmDTO.BruteForceLevel;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//
//
//public class DMoperationalController {
//
////    public Label sliderValueLabel;
////    public TextField taskSizeTextField;
////    @FXML
////    private Slider agentSize;
////
////    @FXML
////    private ComboBox<BruteForceLevel> levelCombobox;
////    @FXML
////    private DecryptionManager decryptionManager;
////    @FXML
////    private Button pauseBotton;
////
////    @FXML
////    private Button stopBotton;
////
////    @FXML
////    private Button startBotton;
//    public void setDecryptionManager(DecryptionManager decryptionManager) {
//      //  this.decryptionManager = decryptionManager;
//    }
//
//
////    @FXML
////    void pauseBFbotton(ActionEvent event) {
////
////
////    }
////
////    @FXML
////    void startBFbotton(ActionEvent event) {
////        //decryptionManager
////    }
////
////    @FXML
////    void stopBFbotton(ActionEvent event) {
////    }
//
//    @FXML
//    private void initialize() {
//
////        agentSize.setMin(2);
////        agentSize.setMax(50);
////        agentSize.setValue(2);
////        agentSize.setBlockIncrement(1);
////        agentSize.setMajorTickUnit(1);
////        agentSize.setMinorTickCount(0);
////        agentSize.setShowTickLabels(true);
////        agentSize.setSnapToTicks(true);
////
////        agentSize.valueProperty().addListener(
////                (observable, oldValue, newValue) -> sliderValueLabel.setText("value: " + newValue.intValue()));
////
////        levelCombobox.getItems().addAll(BruteForceLevel.values());
////        taskSizeTextField.setAccessibleText("Enter task size>=0");
//
//    }
//
//}
package UI.application.DmTab.DMoperational;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class DMoperationalController {

    @FXML
    private Slider agentSize;

    @FXML
    private ComboBox<?> levelCombobox;

    @FXML
    private Button pauseBotton;

    @FXML
    private Button stopBotton;

    @FXML
    private Button startBotton;

    @FXML
    private Label sliderValueLabel;

    @FXML
    private TextField taskSizeTextField;

    @FXML
    void pauseBFbotton(ActionEvent event) {

    }

    @FXML
    void startBFbotton(ActionEvent event) {

    }

    @FXML
    void stopBFbotton(ActionEvent event) {

    }

}
