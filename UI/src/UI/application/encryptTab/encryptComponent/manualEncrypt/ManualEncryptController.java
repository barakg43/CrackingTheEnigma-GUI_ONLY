package UI.application.encryptTab.encryptComponent.manualEncrypt;

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
        private Encryptor encryptor;
        private String input="";
        @FXML
        private void doneGettingInput(ActionEvent event) {
                System.out.println("press done!");
                encryptor.addOutputStringToStatics(input,outputProperty.getValue());
                input="";
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

}