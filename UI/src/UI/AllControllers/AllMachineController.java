package UI.AllControllers;

import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AllMachineController {

    @FXML public Pane FilePathComponent;
    public ScrollPane MachineConfComponent;
    @FXML private FilePathController FilePathComponentController;
    @FXML private MachineConfigurationController MachineConfComponentController;
    @FXML private Label FirstLoadFileLabel;

    private Engine mEngine;

    public AllMachineController(){
        mEngine=new EnigmaEngine();
    }
    public Engine getmEngine() {
        return mEngine;
    }
    public void setmEngine(Engine mEngine) {
        this.mEngine = mEngine;
    }

   @FXML
    public void initialize() {
        if(FilePathComponentController!=null && MachineConfComponentController!=null) {
            FilePathComponentController.setMainAppController(this);
            MachineConfComponentController.setMainAppController(this);
        }
    }
    public void setMachineDetails(){
        MachineConfComponentController.setMachineDetails();
    }

    public void setConfPanel(boolean visible) {
        FirstLoadFileLabel.setVisible(false);
       // FirstLoadFileLabel.setText("File loaded successfully");
    }
    public void setInitializeCodeConf() {
        MachineConfComponentController.setInitializeConfiguration();
    }
}
