package UI.FIlePath;


import UI.AllMachine.AllMachineController;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FilePathController {

    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    public Label SelectedFilePath;
    private AllMachineController mainAppController;

    public void setMainAppController(AllMachineController MainController)
    {
        mainAppController=MainController;
    }
    public FilePathController(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected= new SimpleBooleanProperty();
    }
    @FXML
    private void initialize(){
        SelectedFilePath.textProperty().bind(selectedFileProperty);
    }
    @FXML
    public void LoadFileButtonActionListener(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile == null) {
                return;
            }

            String absolutePath = selectedFile.getAbsolutePath();
            Engine mEngine = new EnigmaEngine();
            try {
                mEngine.loadXMLFile(absolutePath);
                mEngine.resetAllData();
                selectedFileProperty.set(absolutePath);
                mainAppController.setmEngine(mEngine);
                mainAppController.setMachineDetails();
                mainAppController.setConfPanel();
                //mainAppController.setInitializeCodeConf();
                mainAppController.getFirstLoadFileLabel().setText("File loaded successfully.");
                isFileSelected.set(true);
            } catch (Exception ex) {
                mainAppController.getFirstLoadFileLabel().setVisible(true);
                mainAppController.getFirstLoadFileLabel().setText("In file: " + absolutePath +"\n" + ex.getMessage());
            }
        }catch (Exception ex)
        {
            mainAppController.setConfPanel();
        }

    }
}
