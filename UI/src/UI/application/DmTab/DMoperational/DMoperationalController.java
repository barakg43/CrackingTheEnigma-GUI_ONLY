package UI.application.DmTab.DMoperational;

import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.BruteForceLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;

public class DMoperationalController {

    @FXML
    private Slider agentSize;

    @FXML
    private ComboBox<BruteForceLevel> levelCombobox;

    @FXML
    private Spinner<Integer> taskSizeSpinner;
    private DecryptionManager decryptionManager;
    @FXML
    private Button pauseBotton;

    @FXML
    private Button stopBotton;

    @FXML
    private Button startBotton;
    public void setDecryptionManager(DecryptionManager decryptionManager) {
        this.decryptionManager = decryptionManager;
    }


    @FXML
    void pauseBFbotton(ActionEvent event) {


    }

    @FXML
    void startBFbotton(ActionEvent event) {
        //decryptionManager
    }

    @FXML
    void stopBFbotton(ActionEvent event) {

    }

    @FXML
    private void initialize() {

        agentSize.setMax(50);
        agentSize.setMin(2);
        levelCombobox.getItems().addAll(BruteForceLevel.values());

    }

}
