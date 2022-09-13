package UI.application.encryptTab.encryptComponent;

import UI.application.encryptTab.EncryptTabController;
import UI.application.encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import UI.application.encryptTab.encryptComponent.manualEncrypt.ManualEncryptController;
import enigmaEngine.Encryptor;
import enigmaEngine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class EncryptComponentController {

    public Label inputString;

    @FXML public Label alphabetString;

    @FXML
    private AutomaticEncryptController automaticComponentController;
    @FXML
    private Pane automaticLayout;
    @FXML
    private ManualEncryptController manualComponentController;
    @FXML
    private Pane manualLayout;
    @FXML
    private RadioButton automaticToggle;
    @FXML
    private RadioButton manualToggle;
    @FXML
    private Label outputString;
    @FXML
    private Label inputString;
    private Engine encryptor;
    private ToggleGroup toggleGroupSelector;
    private EncryptTabController parentComponentTab;

    public void resetCodeToInitialState(ActionEvent actionEvent) {
        encryptor.resetCodePosition();
        automaticComponentController.clearTextFieldInput(actionEvent);
    }

    public void setParentComponentTab(EncryptTabController parentComponentTab) {
        this.parentComponentTab = parentComponentTab;
    }

    public void setEncryptor(Engine encryptor) {
        alphabetString.setText(encryptor.getMachineData().getAlphabetString());
        this.encryptor = encryptor;
        automaticComponentController.setEncryptor(encryptor);
        manualComponentController.setEncryptor(encryptor);

    }
    public void doneProcessData()
    {
        parentComponentTab.doneProcessData();
        parentComponentTab.getCodeComponentController().setSelectedCode(parentComponentTab.getEnigmaEngine().getCodeFormat(false));
        parentComponentTab.getMainController().bindCurrentCode();

    }
    public void clearAllData()
    {
        automaticComponentController.clearTextFieldInput(new ActionEvent());
        manualComponentController.clearTextField();
        clearInputOutputLabel();

    }
    @FXML
    public void clearInputOutputLabel(){

        outputString.setText("");
        inputString.setText("");
    }
    @FXML
    private void initialize() {
        //link toggle to group
        toggleGroupSelector=new ToggleGroup();
        manualToggle.setToggleGroup(toggleGroupSelector);
        automaticToggle.setToggleGroup(toggleGroupSelector);
        automaticToggle.setSelected(true);
        //link toggle to layout component
        automaticLayout.disableProperty().bind(automaticToggle.selectedProperty().not());
        automaticLayout.visibleProperty().bind(automaticToggle.selectedProperty());
        manualLayout.disableProperty().bind(manualToggle.selectedProperty().not());
        manualLayout.visibleProperty().bind(manualToggle.selectedProperty());
        //link output label to model in controllers

        automaticComponentController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
        manualComponentController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
        //clear output and input when switching between automatic and manual
        automaticToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            clearInputOutputLabel();
        });


        //link child controller to parent
        automaticComponentController.setEncryptComponentController(this);
        manualComponentController.setEncryptComponentController(this);

    }

}
