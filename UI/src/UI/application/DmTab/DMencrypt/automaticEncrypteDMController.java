package UI.application.DmTab.DMencrypt;

import UI.application.encryptTab.EncryptTabController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import UI.application.encryptTab.encryptComponent.manualEncrypt.ManualEncryptController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class automaticEncrypteDMController {


    @FXML public Pane automaticLayout;
    @FXML public Label outputString;
    @FXML public Label inputString;
    @FXML public Label alphabetString;
    private Encryptor encryptor;
    @FXML private GridPane encryptData;
    @FXML private AutomaticEncryptController encryptDataController;
    private encryptTabDMController parentComponentTab;


    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
        encryptDataController.setEncryptor(encryptor);

    }
    public void doneProcessData()
    {
        parentComponentTab.doneProcessData();
        parentComponentTab.getCodeComponentController().setSelectedCode(parentComponentTab.getEnigmaEngine().getCodeFormat(false));
        parentComponentTab.getMainController().bindCurrentCode();
    }
    @FXML
    private void initialize() {

        encryptDataController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
//        //link child controller to parent
        encryptDataController.setAutomaticEncrypteDMController(this);

    }

    public void setParentComponentTab(encryptTabDMController parentComponentTab) {
        this.parentComponentTab = parentComponentTab;
    }

    public void resetCodeToInitialState(ActionEvent actionEvent) {
        encryptor.resetCodePosition();
        encryptDataController.clearTextFieldInput(actionEvent);
    }
}
