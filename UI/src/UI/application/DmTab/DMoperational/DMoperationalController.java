package UI.application.DmTab.DMoperational;

import UI.application.DmTab.DMcontroller;
import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.BruteForceLevel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class DMoperationalController {

    public Label sliderValueLabel;
    public TextField taskSizeTextField;
    @FXML
    private Slider agentSize;

    @FXML
    private ComboBox<BruteForceLevel> levelCombobox;
    @FXML
    private DecryptionManager decryptionManager;

    @FXML
    private Button pauseBotton;

    @FXML
    private Button stopBotton;

    @FXML
    private Button startBotton;

    private DMcontroller DMcontroller;
    public void setDecryptionManager(DecryptionManager decryptionManager) {
      //  this.decryptionManager = decryptionManager;
    }


    @FXML
    void pauseBFbotton(ActionEvent event) {


    }

    @FXML
    void startBFbotton(ActionEvent event) {
        String error=checkIfBFDataValid();
        if(error!=null)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Invalid input data");
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
        }
        else{
            DMcontroller.addCandidates();
        }


    }

    private String checkIfBFDataValid() {
        if(levelCombobox.getSelectionModel().getSelectedIndex()==-1)
        {
            return "Please select difficulty level";
        }
        try{
           int taskSize=  Integer.parseInt(taskSizeTextField.getText());
           if(taskSize<=0)
               return "Please select task size bigger then zero";
        }catch (Exception ex)
        {
            return "Please enter an integer task size";
        }

        return null;
    }

    @FXML
    void stopBFbotton(ActionEvent event) {
    }

    @FXML
    private void initialize() {


        agentSize.setMin(2);
        agentSize.setMax(50);
        agentSize.setValue(2);
        agentSize.setBlockIncrement(1);
        agentSize.setMajorTickUnit(10);
        agentSize.setMinorTickCount(5);
        agentSize.setShowTickLabels(true);
        agentSize.setSnapToTicks(true);

        agentSize.valueProperty().addListener(
                (observable, oldValue, newValue) -> sliderValueLabel.setText("value: " + newValue.intValue()));

        levelCombobox.getItems().addAll(BruteForceLevel.values());

    }

    public void setDMControoler(DMcontroller dMcontroller) {
        this.DMcontroller=dMcontroller;
    }
}

