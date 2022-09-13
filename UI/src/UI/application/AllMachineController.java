package UI.application;

import UI.application.DmTab.DMcontroller;
import UI.application.FIlePathComponent.FilePathController;
import UI.application.MachineConfTab.MachineConfigurationController;
import UI.application.encryptTab.EncryptTabController;

import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.SingleCodeStatisticsViewController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.BruteForceLevel;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AllMachineController {

    @FXML public Pane FilePathComponent;

    @FXML public ScrollPane allMachineScrollPane;

    @FXML
    private ScrollPane DMTabComponent;

    @FXML  public ScrollPane MachineConfComponent;
    @FXML  public Tab encryptionTab;
    @FXML public Tab machineConfTab;
    @FXML public Tab automaticEncryptionTab;
    @FXML private FilePathController FilePathComponentController;
    @FXML private MachineConfigurationController MachineConfComponentController;
    @FXML private DMcontroller DMTabComponentController;
    @FXML private Label FirstLoadFileLabel;
    private Engine mEngine;

    private SingleCodeStatisticsViewController singleCodeController;

    private CodeFormatDTO codeFormatBF;
    @FXML private BorderPane encryptionTabComponent;
    @FXML private EncryptTabController encryptionTabComponentController;
    public AllMachineController(){

        mEngine=new EnigmaEngine();
        encryptionTabComponentController=new EncryptTabController();
    }

    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    public Engine getmEngine() {
        return mEngine;
    }
    public void setmEngine(Engine mEngine) {
        this.mEngine = mEngine;
        encryptionTabComponentController.setEnigmaEngine(mEngine);
        DMTabComponentController.setEnigmaEngine(mEngine);

    }

    public void setSceneWidthHeightProperties(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
        sceneHeightProperty=heightProperty;
        sceneWidthProperty=widthProperty;

        encryptionTabComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        MachineConfComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }
    public Label getFirstLoadFileLabel()
    {
        return FirstLoadFileLabel;
    }
    @FXML
    public void initialize()
    {

        if(FilePathComponentController!=null && MachineConfComponentController!=null && encryptionTabComponentController!=null
        && DMTabComponentController!=null)
        {
            FilePathComponentController.setMainAppController(this);
            MachineConfComponentController.setMainAppController(this);
            encryptionTabComponentController.setMainAppController(this);
            DMTabComponentController.setMainAppController(this);
        }


    }
    public void setMachineDetails(){
        MachineConfComponentController.setMachineDetails();
        DMTabComponentController.setDictionaryList();
    }

    public void setConfPanel() {
        MachineConfComponentController.resetAllFields();
    }
    public void setInitializeCodeConf() {
        MachineConfComponentController.setInitializeConfiguration();
    }

    public void setEncrypteTab() {
        encryptionTabComponentController.setEnigmaEngine(mEngine);
        encryptionTabComponentController.bindTabDisable(MachineConfComponentController.getIsSelected());
    }
    public void setCurrentCode(CodeFormatDTO currentCode)
    {
        encryptionTabComponentController.setSimpleCurrentCode(currentCode);
    }

    public void bindCurrentCode()
    {
        MachineConfComponentController.getCurrentMachineCodeController().setSelectedCode(encryptionTabComponentController.bindCodeComponentController().getCurrentCode());
        MachineConfComponentController.updateCurrentCode();
    }

    public void startBF(CodeFormatDTO codeFormatDTO, BruteForceLevel level)
    {
        codeFormatBF=codeFormatDTO;
        mEngine.bruteForce(codeFormatDTO,level);
    }

    public void bindEncrypteCode()
    {
        encryptionTabComponentController.bindCodeComponentController().setSelectedCode(MachineConfComponentController.CurrentMachineCodeController.getCurrentCode());
    }

}
