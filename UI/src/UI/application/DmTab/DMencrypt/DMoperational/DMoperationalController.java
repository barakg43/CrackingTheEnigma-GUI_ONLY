package UI.application.DmTab.DMencrypt.DMoperational;

import UI.application.DmTab.UIUpdater;
import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.BruteForceLevel;
import enigmaEngine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

import java.util.Optional;

import static java.lang.Thread.sleep;


public class DMoperationalController {

    @FXML
    public Label sliderValueLabel;
    @FXML
    public Spinner<Integer> taskSizeTextSpinner;
    public Button resumeButton;

    @FXML
    private Slider agentSize;

    @FXML
    private ComboBox<BruteForceLevel> levelCombobox;
    private SimpleStringProperty outputString;
    private static DecryptionManager decryptionManager;
     private static UIUpdater uiUpdater;
    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;
    static SimpleBooleanProperty startButtonDisabled;
    private SimpleBooleanProperty pauseButtonDisabled;
    public void setDecryptionManager(DecryptionManager decryptionManager) {
      //  this.decryptionManager = decryptionManager;
    }
    private Engine enigmaEngine;

    public void setUiUpdater(UIUpdater uiUpdater) {
        this.uiUpdater = uiUpdater;
        startButton.disableProperty().bind(Bindings.and(startButtonDisabled, uiUpdater.getIsDoneBruteForceProperty().not()));
        startButtonDisabled.setValue(false);
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

        agentSize.setMin(2);
        agentSize.setMax(enigmaEngine.getAgentsAmount());
        agentSize.setValue(2);
        agentSize.setBlockIncrement(1);
        agentSize.setMajorTickUnit(enigmaEngine.getAgentsAmount()/5.0);
        agentSize.setMinorTickCount(enigmaEngine.getAgentsAmount()/10);
    }


    private String checkIfBFDataValid() {
        if(levelCombobox.getSelectionModel().getSelectedIndex()==-1)
        {
            return "Please select difficulty level";
        }
       if(getAgentAmountFromSpinner()<=0)
           return "Please select task size bigger then zero";


        return null;
    }
    private int getAgentAmountFromSpinner()
    {
        if(taskSizeTextSpinner.getValue()>0)
            return taskSizeTextSpinner.getValue();
        int agentSize= Integer.parseInt(taskSizeTextSpinner.getEditor().getText());
        if(agentSize>0)
            return agentSize;

        return -1;
    }
    @FXML
    void startBFButton(ActionEvent event) {
        String error=checkIfBFDataValid();
        if(error!=null)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Invalid input data");
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
            return;
        }

        startButtonDisabled.setValue(true);
        decryptionManager.setSetupConfiguration(levelCombobox.getValue(),
                (int) agentSize.getValue(),
                getAgentAmountFromSpinner());
        // DMcontroller.addCandidates();

        uiUpdater.setupCandidateListener();
        decryptionManager.startBruteForce(outputString.getValue());
        pauseButtonDisabled.set(true);


    }
    @FXML
    void stopBFButton(ActionEvent event) {
        startButtonDisabled.setValue(false);
        resumeButton.setDisable(true);
        pauseButtonDisabled.set(true);
        decryptionManager.stop();
        uiUpdater.stopCandidateListener();
    }
    @FXML
    void pauseBFButton(ActionEvent event) {
        resumeButton.setDisable(false);
        pauseButtonDisabled.setValue(true);
        decryptionManager.pause();
        uiUpdater.pauseCandidateListener();
    }
    public void resumeButtonOnAction(ActionEvent actionEvent) {
        pauseButtonDisabled.setValue(false);
        resumeButton.setDisable(true);
        decryptionManager.resume();
        uiUpdater.resumeCandidateListener();
    }

    @FXML
    private void initialize() {

        sliderValueLabel.setText("value: 2 ");

        SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE);
        integerSpinnerValueFactory.setAmountToStepBy(10);
        integerSpinnerValueFactory.setValue(0);
        taskSizeTextSpinner.setValueFactory(integerSpinnerValueFactory);
        taskSizeTextSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        taskSizeTextSpinner.editorProperty().get().setAlignment(Pos.CENTER);
        agentSize.setShowTickLabels(true);
        agentSize.setSnapToTicks(true);
        outputString=new SimpleStringProperty();
        agentSize.valueProperty().addListener(
                (observable, oldValue, newValue) -> sliderValueLabel.setText("value: " + newValue.intValue()));
        startButtonDisabled =new SimpleBooleanProperty(true);
        pauseButtonDisabled=new SimpleBooleanProperty(true);
        levelCombobox.getItems().addAll(BruteForceLevel.values());
        pauseButton.disableProperty().bind(Bindings.and(pauseButtonDisabled,startButton.disabledProperty().not()));
        stopButton.disableProperty().bind(startButton.disabledProperty().not());
       //resumeButton.disableProperty().bind(Bindings.and(pauseButton.disabledProperty().not(),stopButton.disabledProperty()));
        startButtonDisabled.setValue(false);
    }

    public static void closeWindowEvent(WindowEvent event) {
        System.out.println("Window close request ...");

        if(UIUpdater.isCandidateLinterAlive()) {  // if the dataset has changed, alert the user with a popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Quit application");
            alert.setContentText(String.format("Decryption tasks still running,do you want to close?"));
            // alert.initOwner(primaryStage.getOwner());
            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent()) {
                if(res.get().equals(ButtonType.CANCEL)) {
                    event.consume();
                }
                else if(res.get().equals(ButtonType.YES))
                {
                    decryptionManager.stop();
                    uiUpdater.stopCandidateListener();
                }
            }
        }
    }

}

