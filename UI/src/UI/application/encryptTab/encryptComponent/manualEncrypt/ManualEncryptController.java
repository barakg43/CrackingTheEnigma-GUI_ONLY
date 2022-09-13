package UI.application.encryptTab.encryptComponent.manualEncrypt;

import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class ManualEncryptController {


        @FXML
        private TextField textInputField;
        private StringProperty outputProperty;
        private StringProperty inputProperty;
        private Encryptor encryptor;
        private String input="";
        private EncryptComponentController encryptComponentController;
        public void setEncryptComponentController(EncryptComponentController encryptComponentController) {
                this.encryptComponentController = encryptComponentController;
        }
        @FXML
        private void doneGettingInput(ActionEvent event) {
               // System.out.println("press done!");
                encryptor.addOutputStringToStatics(input,outputProperty.getValue());
                inputProperty.setValue(textInputField.getText().toUpperCase());
                input="";
                encryptComponentController.doneProcessData(); //Pop up process done to parent
        }
//        public void clearTextFieldInput(ActionEvent event) {
//                textInputField.clear();
//        }
        @FXML
        private void processSingleCharacter(KeyEvent event) {
                System.out.println("enter key");
                input=textInputField.getText();
              //  outputProperty.setValue(outputProperty.getValue()+textInputField.getText());
               textInputField.clear();
        }
        public void bindOutputPropertyFromParent(StringProperty outputPropertyParent) {
                outputProperty=outputPropertyParent;
        }
        public void setEncryptor(Encryptor encryptor) {
                this.encryptor = encryptor;
        }

        public void bindInputPropertyFromParent(StringProperty inputPropertyParent) {
                inputProperty=inputPropertyParent;
        }
}