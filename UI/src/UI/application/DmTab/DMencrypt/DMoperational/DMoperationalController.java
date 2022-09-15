package UI.application.DmTab.DMencrypt.DMoperational;

import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.UIUpdater;
import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.BruteForceLevel;
import enigmaEngine.Engine;
import javafx.beans.property.SimpleStringProperty;
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
    private SimpleStringProperty outputString;
    private DecryptionManager decryptionManager;
    private UIUpdater uiUpdater;
    @FXML
    private Button pauseBotton;

    @FXML
    private Button stopBotton;

    @FXML
    private Button startBotton;


    public void setDecryptionManager(DecryptionManager decryptionManager) {
      //  this.decryptionManager = decryptionManager;
    }
    private Engine enigmaEngine;

    public void setUiUpdater(UIUpdater uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    @FXML
    void pauseBFbotton(ActionEvent event) {


    }
    public void bindOutputStringToParent(SimpleStringProperty outputParent)
    {
        outputString.bind(outputParent);

    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        decryptionManager=new DecryptionManager(enigmaEngine);
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
            decryptionManager.setSetupConfiguration(levelCombobox.getValue(),
                    (int) agentSize.getValue(),
                    Integer.parseInt(taskSizeTextField.getText()));
           // DMcontroller.addCandidates();
            uiUpdater.startCandidateListener();
            uiUpdater.getProgressDataDTO().progressBarProperty().set(0);  //TODO
            decryptionManager.startBruteForce(outputString.getValue());
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

        sliderValueLabel.setText("value: 2 ");
        agentSize.setMin(2);
        agentSize.setMax(50);
        agentSize.setValue(2);
        agentSize.setBlockIncrement(1);
        agentSize.setMajorTickUnit(10);
        agentSize.setMinorTickCount(5);
        agentSize.setShowTickLabels(true);
        agentSize.setSnapToTicks(true);
        outputString=new SimpleStringProperty();
        agentSize.valueProperty().addListener(
                (observable, oldValue, newValue) -> sliderValueLabel.setText("value: " + newValue.intValue()));

        levelCombobox.getItems().addAll(BruteForceLevel.values());

    }


}

