package UI.application.encryptTab.encryptComponent.manualEncrypt;

import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class ManualEncryptController {


        @FXML
        private TextField textInputField;
        private StringProperty outputProperty;
        private StringProperty inputProperty;
        private Encryptor encryptor;
        private EncryptComponentController encryptComponentController;
        public void setEncryptComponentController(EncryptComponentController encryptComponentController) {
                this.encryptComponentController = encryptComponentController;
        }
        @FXML
        private void doneGettingInput(ActionEvent event) {
               // System.out.println("press done!");

                encryptor.addOutputStringToStatics(inputProperty.getValue(),outputProperty.getValue());
                //inputProperty.setValue(textInputField.getText().toUpperCase());
             /// outputProperty.setValue(outputProperty.getValue()+textInputField.getText());
            clearTextField();
            encryptComponentController.clearAllData();
            encryptComponentController.doneProcessData(); //Pop up process done to parent

        }
//        public void clearTextFieldInput(ActionEvent event) {
//                textInputField.clear();
//        }
    public void clearTextField(){
        textInputField.clear();
        textInputField.clear();

    }
        @FXML
        private void processSingleCharacter(KeyEvent event) {
             //  System.out.println("pressed key:"+event.getCharacter()+" length: "+ event.getCharacter().length());
             try {
                     char outputProcessChar=encryptor.processDataInput(event.getCharacter().charAt(0));
                     inputProperty.setValue(inputProperty.getValue()+event.getCharacter().toUpperCase());
                     outputProperty.setValue(outputProperty.getValue()+outputProcessChar);
                 }
             catch (Exception ex)
             {
                 Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                 errorAlert.setTitle("Error");
                 errorAlert.setHeaderText("Invalid input string");
                 errorAlert.setContentText(ex.getMessage());
                 errorAlert.showAndWait();
             }
             finally {
                 clearTextField();
             }

        }
        public void bindInputOutputPropertyFromParent(StringProperty inputPropertyParent,StringProperty outputPropertyParent) {
                outputProperty=outputPropertyParent;
                inputProperty=inputPropertyParent;
        }
        public void setEncryptor(Encryptor encryptor) {
                this.encryptor = encryptor;
        }

        public void bindInputPropertyFromParent(StringProperty inputPropertyParent) {
                inputProperty=inputPropertyParent;
        }
}