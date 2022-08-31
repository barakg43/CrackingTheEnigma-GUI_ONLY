package UI.application.encryptTab.encryptComponent.automaticEncrypt;






import java.net.URL;
import java.util.ResourceBundle;

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

        @FXML
        void processStringData(ActionEvent event) {
            outputProperty.setValue( encryptor.processDataInput(stringInputTextField.getText()));
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
        void initialize() {

            assert processEncryptButton != null : "fx:id=\"processEncryptButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert clearTextFieldButton != null : "fx:id=\"clearTextFieldButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert stringInputTextField != null : "fx:id=\"stringInput\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";

        }

    }





