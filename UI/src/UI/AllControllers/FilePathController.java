package UI.AllControllers;


import UI.AllControllers.AllMachineController;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FilePathController {

    private SimpleStringProperty selectedFileProperty;
    public Label fileSuccessfullyLabel;
    public Label SelectedFilePath;
    private AllMachineController mainAppController;
    @FXML
    private Button LoadFileButton;

    public void setMainAppController(AllMachineController MainController)
    {
        mainAppController=MainController;
    }
    public FilePathController(){
        selectedFileProperty = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        SelectedFilePath.textProperty().bind(selectedFileProperty);
    }
    @FXML
    public void LoadFileButtonActionListener(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile == null) {
                return;
            }

            String absolutePath = selectedFile.getAbsolutePath();
            selectedFileProperty.set(absolutePath);

            Engine mEngine=new EnigmaEngine();
            mEngine.loadXMLFile(absolutePath);
            mainAppController.setmEngine(mEngine);
            mainAppController.setConfPanel(false);
            mainAppController.setMachineDetails();
            mainAppController.setInitializeCodeConf();

        }catch (Exception ex)
        {
            mainAppController.setConfPanel(false);
        }

    }
}
