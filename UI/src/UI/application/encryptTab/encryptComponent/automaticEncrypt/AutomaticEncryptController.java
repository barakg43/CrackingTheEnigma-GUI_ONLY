package UI.application.encryptTab.encryptComponent.automaticEncrypt;






import java.net.URL;
import java.util.ResourceBundle;

import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AutomaticEncryptController {

        Encryptor encryptor;
        @FXML
        private Button processEncryptButton;

        @FXML
        private Button clearTextFieldButton;
        @FXML
        private TextField stringInputTextField;
        private StringProperty outputProperty;



    private EncryptComponentController encryptComponentController;
    public void setEncryptComponentController(EncryptComponentController encryptComponentController) {
        this.encryptComponentController = encryptComponentController;
    }
        @FXML
        void processStringData(ActionEvent event) {
            outputProperty.setValue( encryptor.processDataInput(stringInputTextField.getText()));//update output label on component
            encryptComponentController.doneProcessData(); //Pop up process done to parent
        }
        @FXML public void clearTextFieldInput(ActionEvent event) {
            stringInputTextField.clear();
        }

        public void setEncryptor(Encryptor encryptor) {
            this.encryptor = encryptor;
        }

        public void bindOutputPropertyFromParent(StringProperty outputPropertyParent) {
            outputProperty=outputPropertyParent;
        }

    @FXML
    private void initialize() {

            assert processEncryptButton != null : "fx:id=\"processEncryptButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert clearTextFieldButton != null : "fx:id=\"clearTextFieldButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert stringInputTextField != null : "fx:id=\"stringInput\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";

        }

    }





