package UI.application.encryptTab.encryptComponent.automaticEncrypt;


import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        private StringProperty inputProperty;



    private EncryptComponentController encryptComponentController;
    public void setEncryptComponentController(EncryptComponentController encryptComponentController) {
        this.encryptComponentController = encryptComponentController;
    }
        @FXML
        void processStringData(ActionEvent event) {
            try {
                if(stringInputTextField.getText().length()==0)
                    throw new RuntimeException("Cannot process empty string!");

                outputProperty.setValue( encryptor.processDataInput(stringInputTextField.getText()));//update output label on component
                inputProperty.setValue(stringInputTextField.getText().toUpperCase());
                encryptComponentController.doneProcessData();   //Pop up process done to parent
            }
            catch (RuntimeException ex)
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid input!");
                errorAlert.setContentText(ex.getMessage());
                errorAlert.showAndWait();
            }
        }
        @FXML public void clearTextFieldInput(ActionEvent event) {

        stringInputTextField.clear();
        encryptComponentController.clearInputOutputLabel();
        }

        public void setEncryptor(Encryptor encryptor) {
            this.encryptor = encryptor;
        }

        public void bindInputOutputPropertyFromParent(StringProperty inputPropertyParent,StringProperty outputPropertyParent) {
            outputProperty=outputPropertyParent;
            inputProperty=inputPropertyParent;
        }

    @FXML
    private void initialize() {

            assert processEncryptButton != null : "fx:id=\"processEncryptButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert clearTextFieldButton != null : "fx:id=\"clearTextFieldButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert stringInputTextField != null : "fx:id=\"stringInput\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";

        }

    }





