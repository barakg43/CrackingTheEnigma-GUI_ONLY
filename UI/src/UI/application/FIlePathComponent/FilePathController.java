package UI.application.FIlePathComponent;

import UI.application.AllMachineController;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
     //   selectedFileProperty = new SimpleStringProperty();
       // isFileSelected= new SimpleBooleanProperty();
    }
    @FXML
    private void initialize(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected= new SimpleBooleanProperty();
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
                System.out.println("before set");
                selectedFileProperty.set(absolutePath);
                System.out.println("after set1");
                mainAppController.setmEngine(mEngine);
                System.out.println("after set");
                mainAppController.setMachineDetails();
                System.out.println("after set2");
                mainAppController.setConfPanel();
                System.out.println("after set3");
                //mainAppController.setInitializeCodeConf();
                mainAppController.getFirstLoadFileLabel().setText("File loaded successfully.");
                System.out.println("after set4");
                isFileSelected.set(true);
            } catch (Exception ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid file details");
                errorAlert.setContentText("In file " + selectedFile.getPath() +"\n\n" + ex.getMessage());
                errorAlert.showAndWait();
                //mainAppController.getFirstLoadFileLabel().setVisible(true);
                //mainAppController.getFirstLoadFileLabel().setText("In file: " + absolutePath +"\n" + ex.getMessage());
            }
        }catch (Exception ex)
        {
            mainAppController.setConfPanel();
        }

    }
}
