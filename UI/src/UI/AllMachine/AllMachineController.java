package UI.AllMachine;

import UI.FIlePath.FilePathController;
import UI.MachineConfTab.MachineConfigurationController;
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

    public Label getFirstLoadFileLabel()
    {
        return FirstLoadFileLabel;
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

    public void setConfPanel() {
        MachineConfComponentController.resetAllFields();
    }
    public void setInitializeCodeConf() {
        MachineConfComponentController.setInitializeConfiguration();
    }
}
