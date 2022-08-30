<<<<<<<< HEAD:UI/src/UI/application/AllMachineController.java
package UI.application;

import UI.application.machineTab.machineConfiguration.MachineConfigurationController;
import UI.application.fileLoaderComponent.FilePathController;
========
package UI.AllMachine;

import UI.FIlePath.FilePathController;
import UI.MachineConfTab.MachineConfigurationController;
>>>>>>>> Nikol:UI/src/UI/AllMachine/AllMachineController.java
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

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
    public void initialize()
    {
        if(FilePathComponentController!=null && MachineConfComponentController!=null)
        {
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
