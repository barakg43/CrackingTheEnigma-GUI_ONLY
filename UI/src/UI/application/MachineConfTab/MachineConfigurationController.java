package UI.application.MachineConfTab;


import UI.application.AllMachineController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MachineConfigurationController {

    @FXML  private HBox machineDetailsComponent;
   @FXML private MachineDetailsController machineDetailsComponentController;
    @FXML  private ScrollPane CodeCalibrationComponent;
    @FXML  private CodeCalibrationController CodeCalibrationComponentController;
    @FXML  private ScrollPane scrollPanelVboxPlugboard;
    @FXML  private ScrollPane scrollPaneHboxRotorPosition;
   // private HBox hboxRotorPosition;
    @FXML private AnchorPane CurrentCodeComponent;
    @FXML private NewCodeFormatController CurrentCodeComponentController;
   @FXML private AnchorPane CurrentCodeConfigurationPane;
 //   @FXML private HBox rotorsAndPositionsHBox;
    // Tab: plugBoard pairs
    @FXML private SplitPane machineConfSplitPane;
    @FXML private AnchorPane selectedCodeAnchorePane;

    @FXML  private SplitPane ConfigurationPanel;
    @FXML private ScrollPane currentCodeConfigScrollPane;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;
    private SimpleBooleanProperty isCodeSelectedByUser;
    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty showCodeDetails;
    private ObservableList<SimpleStringProperty> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> currentRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedPlugBoardPairsProperty;

    private ReadOnlyDoubleProperty widthProperty;

    @FXML
    public void initialize() {

        if (CurrentCodeComponentController != null && CodeCalibrationComponentController != null &&
                machineDetailsComponentController != null) {
            CurrentCodeComponentController.SetMachineConfController(this);
            CodeCalibrationComponentController.SetMachineConfController(this);
            machineDetailsComponentController.SetMachineConfController(this);
//            SelectedMachineCodeController.SetMachineConfController(this);
            //          CurrentMachineCodeController.SetMachineConfController(this);

        }
        //  createRotorAreaCodeConf();
        //  createPlugAreaCodeConf();

        CurrentCodeConfigurationPane.disableProperty().bind(isSelected.not());

    }
    private void createRotorAreaCodeConf(){
       // rotorsAndPositionsHBox=new HBox();
      //  scrollPaneHboxRotorPosition=new ScrollPane(rotorsAndPositionsHBox);
        scrollPaneHboxRotorPosition.setPrefWidth(105);
        scrollPaneHboxRotorPosition.setPrefHeight(390);
       // rotorConfCode.getChildren().add(scrollPaneHboxRotorPosition);
    }
    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {
        this.widthProperty=widthProperty;
        ConfigurationPanel.prefWidthProperty().bind(widthProperty);
        //CurrentCodeComponent.prefHeightProperty().bind(heightProperty);
        CurrentCodeComponent.prefWidthProperty().bind(widthProperty);
        currentCodeConfigScrollPane.prefWidthProperty().bind(widthProperty);
        currentCodeConfigScrollPane.prefHeightProperty().bind(heightProperty);
        machineDetailsComponentController.bindMachineDetails(widthProperty);

    }


    public MachineConfigurationController()
    {
        //numberOfPairs=0;
        isCodeSelectedByUser=new SimpleBooleanProperty();
        isSelected=new SimpleBooleanProperty(false);
        showCodeDetails=new SimpleBooleanProperty(false);
      //  selectedReflectorProperty=new SimpleStringProperty();
      //  currentReflectorProperty=new SimpleStringProperty();
        selectedRotorsIDProperty=FXCollections.observableArrayList();
        currentRotorsIDProperty=FXCollections.observableArrayList();
        selectedPlugBoardPairsProperty=FXCollections.observableArrayList();
    }

    public SimpleBooleanProperty getIsSelected()
    {
        return isSelected;
    }
    public Engine getmEngine() {
        return mEngine;
    }

    public void setMainAppController(AllMachineController MainController) {
        mainAppController = MainController;
        mEngine = new EnigmaEngine();
      //  mainAppController.bindEncryptionTabComponents(currentReflectorProperty,currentRotorsIDProperty);
    }

    public void setMachineDetails() {


        isSelected.set(false);
        for (SimpleStringProperty rotorid: currentRotorsIDProperty) {
            rotorid.set("");
        }
        showCodeDetails.set(false);
        machineDetailsComponentController.clearCurrentCode();
        mEngine = mainAppController.getmEngine();
        machineData = mEngine.getMachineData();
        CodeCalibrationComponentController.createDataMachineSets();
        if (machineData != null) {
           machineDetailsComponentController.setData();
        }
        if (mEngine.isCodeConfigurationIsSet()) {
            machineDetailsComponentController.setCodes();
            setVisibleCodeFields(true);
        } else {
            setVisibleCodeFields(false);
        }
    }
    public void increasedTotalCipheredData()
    {
        machineDetailsComponentController.setCipheredInputsData();
    }



    private void setVisibleCodeFields(boolean toVisible) {
       // SelectedMachineCode.setVisible(toVisible);
    }

    public void setInitializeConfiguration() {

        CodeCalibrationComponentController.getFirstInputVBox().getChildren().clear();
        CodeCalibrationComponentController.getSecondInputVBox().getChildren().clear();
        isCodeSelectedByUser.set(false);
        CodeCalibrationComponentController.getMachineCodePane().setVisible(true);
        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
//        int[] rotorsId = machineData.getRotorsId();
//        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            CodeCalibrationComponentController.createRotorInfoComboBox();
        }
        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorIDs = FXCollections.observableArrayList(reflectorIDName);
//        reflectorID.addAll(reflectorIDName);
        CodeCalibrationComponentController.getSelectedReflectorComboBox().setItems(reflectorIDs);
    }

    public void showAllCodes()
    {
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);

        //SelectedMachineCodeController.setSelectedCode(selectedCode);
        CodeCalibrationComponentController.getCodeConfTabPane().getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        //CurrentMachineCodeController.setSelectedCode(currentCode);
        machineDetailsComponentController.setCodes();
        setVisibleCodeFields(true);
        // CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
        mainAppController.setCurrentCode(currentCode);
    }
    public void resetAllFields()
    {

        CodeCalibrationComponentController.resetSelectedData();
        CodeCalibrationComponentController.getSelectedReflectorComboBox().getItems().clear();

        VBox firstInputs=(VBox)CodeCalibrationComponentController.getPairsHBox().getChildren().get(0);
        VBox secondInputs=(VBox)CodeCalibrationComponentController.getPairsHBox().getChildren().get(1);
        for (int i = 0; i < firstInputs.getChildren().size(); i++) {
            ChoiceBox<Character> firstInputFromPair = (ChoiceBox<Character>)firstInputs.getChildren().get(i);
            ChoiceBox<Character> secondInputFromPair = (ChoiceBox<Character>)secondInputs.getChildren().get(i);
            for (int j = 0; j <firstInputFromPair.getItems().size() ; j++) {
                firstInputFromPair.getItems().remove(j);
                secondInputFromPair.getItems().remove(j);
            }
            firstInputFromPair.getItems().clear();
            secondInputFromPair.getItems().clear();
        }
        firstInputs.getChildren().clear();;
        secondInputs.getChildren().clear();

        CurrentCodeComponentController.resetFields();
        CodeCalibrationComponentController.getCodeConfTabPane().getSelectionModel().select(0);
        setInitializeConfiguration();

        if(mEngine.isCodeConfigurationIsSet())
        {
            mEngine.resetAllData();
            mEngine.resetSelected();
            isSelected.set(false);

            machineDetailsComponentController.clearCodes();
        }

        isCodeSelectedByUser.set(false);
    }

    public void updateCurrentCode() {
        CodeFormatDTO currentCode= machineDetailsComponentController.getCurrentMachineCodeController().getCurrentCode();
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
    }

    public SimpleBooleanProperty getShowCodeDetails() {
        return showCodeDetails;
    }

    public SimpleBooleanProperty getIsCodeSelectedByUser() {
        return isCodeSelectedByUser;
    }

    public MachineDetailsController getMachineDetailsController() {
        return machineDetailsComponentController;
    }
}
