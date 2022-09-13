package UI.application.MachineConfTab;


import UI.application.AllMachineController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import dtoObjects.*;
import dtoObjects.DmDTO.BruteForceLevel;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class MachineConfigurationController {

   @FXML public AnchorPane MachineDetails;
    @FXML  public Label NumberOfRotors;
    @FXML  public Label numberOfReflectors;
    @FXML  public Label CipheredInputs;
    @FXML public Pane MachineCodePane;

    @FXML public ComboBox SelectedReflectorComboBox;
    @FXML public Button SetCodeConfButton;
    @FXML public HBox SelectedMachineCode;
    @FXML public SimpleCodeController SelectedMachineCodeController;

    @FXML public NewCodeFormatController CurrentCodeComponentController;
    @FXML   public SimpleCodeController CurrentMachineCodeController;
    @FXML public HBox CurrentMachineCode;

    @FXML public AnchorPane CurrentCodeConfigurationPane;
    @FXML public HBox rotorsAndPositionsHBox;
    // Tab: plugBoard pairs
    @FXML public HBox PairsHBox;
    @FXML  public VBox firstInputVBox;
    @FXML  public VBox secondInputVBox;
    @FXML public Button AddMorePairsButton;
    @FXML public TabPane CodeConfTabPane;
    @FXML   public ScrollPane selectedCodeConfiguration;
    @FXML public Button removePlugBoardPairButton;
    @FXML  public ScrollPane currentCodeScrollPane;
    @FXML  public ScrollPane selectedCodeConfigScrollPane;
    @FXML  public Label selectedMachineCodeLabel;
    @FXML  public Label currentMachineLabel;
    public ScrollPane ConfPane;
    public SplitPane machineConfSplitPane;
    public AnchorPane selectedCodeAnchorePane;
    public SplitPane currentCodeConfSplitPane;
    public HBox ButtonsPane;

    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;
    private int numberOfPairs;
    SplitPane AllMachineSplitPane;

    private SimpleBooleanProperty isCodeSelectedByUser;
    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty showCodeDetails;
    //private SimpleStringProperty selectedReflectorProperty;
   // private SimpleStringProperty currentReflectorProperty;
   // private SimpleListProperty<Integer> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> currentRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedPlugBoardPairsProperty;



    @FXML
    public void initialize() {

        if (CurrentCodeComponentController != null && CurrentMachineCodeController!=null && SelectedMachineCodeController!=null) {
            CurrentCodeComponentController.SetMachineConfController(this);
            SelectedMachineCodeController.SetMachineConfController(this);
            CurrentMachineCodeController.SetMachineConfController(this);

        }

        SetCodeConfButton.disableProperty().bind(isCodeSelectedByUser.not());
        selectedCodeConfigScrollPane.disableProperty().bind(showCodeDetails.not());
        selectedCodeConfigScrollPane.visibleProperty().bind(isSelected);
        currentCodeScrollPane.disableProperty().bind(showCodeDetails.not());
        currentCodeScrollPane.visibleProperty().bind(isSelected);
        selectedMachineCodeLabel.visibleProperty().bind(showCodeDetails);
        currentMachineLabel.visibleProperty().bind(showCodeDetails);
        selectedCodeConfiguration.disableProperty().bind(showCodeDetails.not());
        CurrentCodeConfigurationPane.disableProperty().bind(isSelected.not());

       // SelectedMachineCodeController.getReflectorIDtext().textProperty().bind(selectedReflectorProperty);

      //  CurrentMachineCodeController.getReflectorIDtext().textProperty().bind(currentReflectorProperty);
       // CurrentCodeComponentController.currCodeController.getReflectorIDtext().textProperty().bind(currentReflectorProperty);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {
        ConfPane.prefWidthProperty().bind(widthProperty);
        ConfPane.prefHeightProperty().bind(heightProperty);
        MachineDetails.prefWidthProperty().bind(ConfPane.widthProperty());
        machineConfSplitPane.prefWidthProperty().bind(ConfPane.widthProperty());
        machineConfSplitPane.prefHeightProperty().bind(ConfPane.heightProperty());
//        selectedCodeAnchorePane.prefWidthProperty().bind(machineConfSplitPane.widthProperty());
//        selectedCodeConfiguration.prefWidthProperty().bind(selectedCodeAnchorePane.widthProperty());
//        currentCodeConfSplitPane.prefWidthProperty().bind(selectedCodeConfiguration.widthProperty());
//        currentCodeConfSplitPane.prefHeightProperty().bind(selectedCodeConfiguration.widthProperty());

        CurrentCodeConfigurationPane.prefWidthProperty().bind(currentCodeConfSplitPane.widthProperty());

    }
    public SimpleCodeController getCurrentMachineCodeController()
    {
        return CurrentMachineCodeController;
    }

//    public SimpleStringProperty getSelectedReflector() {
//        return selectedReflectorProperty;
//    }


    public MachineConfigurationController()
    {
        numberOfPairs=0;
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
        CurrentMachineCodeController.clearCurrentCodeView();
        mEngine = mainAppController.getmEngine();
        machineData = mEngine.getMachineData();
        if (machineData != null) {
            NumberOfRotors.setText(machineData.getNumberOfRotorsInUse() + "/" + machineData.getNumberOfRotorInSystem());
            numberOfReflectors.setText(String.valueOf(machineData.getNumberOfReflectors()));
            CipheredInputs.setText(String.valueOf(mEngine.getCipheredInputsAmount()));
            MachineDetails.setDisable(false);
        }
        if (mEngine.isCodeConfigurationIsSet()) {
            CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);
            SelectedMachineCodeController.setSelectedCode(selectedCode);
            CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
            CurrentMachineCodeController.setSelectedCode(currentCode);
            setVisibleCodeFields(true);
        } else {
            setVisibleCodeFields(false);
        }
    }

    private void setVisibleCodeFields(boolean toVisible) {
        SelectedMachineCode.setVisible(toVisible);
    }

    public void setInitializeConfiguration() {

        numberOfPairs=0;
        isCodeSelectedByUser.set(false);
        MachineCodePane.setVisible(true);
        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
        int[] rotorsId = machineData.getRotorsId();
        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            createChoiceBox(rotorsId, positions);
        }
        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorID = FXCollections.observableArrayList();
        for (String ref : reflectorIDName) {
            reflectorID.add(ref);
        }
        SelectedReflectorComboBox.setItems(reflectorID);
    }


    private void createChoiceBox(int[] rotorsID, String positions) {
        VBox Vbox = new VBox();
        Vbox.getChildren().add(SetRotorChoiceBox(rotorsID));
        Vbox.getChildren().add(SetPositionsChoiceBox(positions));
        Vbox.setSpacing(30);
        rotorsAndPositionsHBox.getChildren().add(Vbox);
        rotorsAndPositionsHBox.setSpacing(40);
    }

    private ChoiceBox<Integer> SetRotorChoiceBox(int[] rotorsID) {
        ObservableList<Integer> rotorsIDList = FXCollections.observableArrayList();
        ;
        ChoiceBox<Integer> rotorsChoiceBox = new ChoiceBox<>();
        for (int i = 0; i < rotorsID.length; i++) {
            rotorsIDList.add(rotorsID[i]);
        }
        rotorsChoiceBox.getItems().addAll(rotorsIDList);
        return rotorsChoiceBox;
    }

    private ChoiceBox<Character> SetPositionsChoiceBox(String positions) {
        ChoiceBox<Character> positionsChoiceBox = new ChoiceBox<>();
        ObservableList<Character> positionsList = FXCollections.observableArrayList();
        for (int i = 0; i < positions.length(); i++) {
            positionsList.add(positions.charAt(i));
        }
        positionsChoiceBox.getItems().addAll(positionsList);
        return positionsChoiceBox;
    }


    @FXML
    public void SetCodeConfActionListener(javafx.event.ActionEvent actionEvent) {

        isSelected.set(true);

        List<Integer> selectedRotorsID = new ArrayList<>();
        List<Character> selectedPositions =new ArrayList<>();
        try {
            for (int i = 0; i < rotorsAndPositionsHBox.getChildren().size(); i++) {
                VBox rotorAndPosition=(VBox) rotorsAndPositionsHBox.getChildren().get(i);
                if(rotorAndPosition==null)
                    throw new Exception("You need to configurate all data.");
                if(((ChoiceBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                int selectedID=((ChoiceBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ChoiceBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                Character selectedPosition=((ChoiceBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem();

                selectedRotorsID.add(selectedID);
                selectedPositions.add(selectedPosition);
            }

            mEngine.checkIfRotorsValid(selectedRotorsID);
            mEngine.checkIfPositionsValid(selectedPositions);

            if(SelectedReflectorComboBox.getSelectionModel().getSelectedIndex()==-1)
                throw new Exception("You need to select reflector.");
            String selectedReflector = (String) SelectedReflectorComboBox.getValue();
            mEngine.setReflector(selectedReflector);
         //   selectedReflectorProperty.set(selectedReflector);

            List<PlugboardPairDTO> plugBoardPairs=new ArrayList<>();


            for(int i=0;i<numberOfPairs;i++)
            {
                if(((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character firstInput = ((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                if(((ChoiceBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character secondInput=((ChoiceBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                plugBoardPairs.add(new PlugboardPairDTO(firstInput,secondInput));
            }
            mEngine.checkPlugBoardPairs(plugBoardPairs);

            CurrentMachineCodeController.clearCurrentCodeView();
            SelectedMachineCodeController.clearCurrentCodeView();


            showAllCodes();
            isCodeSelectedByUser.set(false);

//            RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[3];
//            List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
//            rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
//            rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
//            rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
//            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",plugboardPairDTOList);
//
//            System.out.println("starting brute force");
//            mainAppController.startBF(codeFormatDTO, bruteForceLevel.easyLevel);
//            System.out.println("After brute force");


        }catch (Exception ex)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid configuration");
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();

            resetAllFields();
            CodeConfTabPane.getSelectionModel().select(0);

        }


    }

    private void showAllCodes()
    {
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);

        SelectedMachineCodeController.setSelectedCode(selectedCode);
        CodeConfTabPane.getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        CurrentMachineCodeController.setSelectedCode(currentCode);
        setVisibleCodeFields(true);
        // CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
        mainAppController.setCurrentCode(currentCode);
    }

    private ChoiceBox<Character> SetPairsChoiceBox(String alphabet) {
        ChoiceBox<Character> pairsChoiceBox = new ChoiceBox<>();
        ObservableList<Character> alphabetList = FXCollections.observableArrayList();
        for (int i = 0; i < alphabet.length(); i++) {
            alphabetList.add(alphabet.charAt(i));
        }
        pairsChoiceBox.setPrefSize(70, 5);
        pairsChoiceBox.getItems().addAll(alphabetList);
        return pairsChoiceBox;
    }

    private void setPlugBoardPairs()
    {
        String alphabet=machineData.getAlphabetString();
        firstInputVBox.getChildren().add(SetPairsChoiceBox(alphabet));
        secondInputVBox.getChildren().add(SetPairsChoiceBox(alphabet));
        PairsHBox.setSpacing(20);
        firstInputVBox.setSpacing(10);
        secondInputVBox.setSpacing(10);
        numberOfPairs++;
        if(numberOfPairs==alphabet.length()/2)
            AddMorePairsButton.setDisable(true);
        else
            AddMorePairsButton.setDisable(false);
    }

    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        resetAllFields();
        disableAllFields(true);
        mEngine.resetSelected();
        mEngine.setCodeAutomatically();
        showCodeDetails.set(true);
        isSelected.set(true);
        CurrentMachineCodeController.clearCurrentCodeView();
        SelectedMachineCodeController.clearCurrentCodeView();
        showAllCodes();
        disableAllFields(false);

        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[3];
        List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
        rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
        rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
        rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",plugboardPairDTOList);

        System.out.println("starting brute force");
        mainAppController.startBF(codeFormatDTO, BruteForceLevel.easyLevel);
        System.out.println("After brute force");
    }

    public void resetAllFields()
    {
        for (int i = 0; i <  rotorsAndPositionsHBox.getChildren().size(); i++) {
           VBox rotorAndPositionVBOx= (VBox)(rotorsAndPositionsHBox.getChildren().get(i));
           ChoiceBox<Integer> rotorID = (ChoiceBox<Integer>) (rotorAndPositionVBOx.getChildren().get(0));
            ChoiceBox<Character> positions = (ChoiceBox<Character>) (rotorAndPositionVBOx.getChildren().get(1));
            for (int j = 0; j <rotorID.getItems().size() ; j++) {
                rotorID.getItems().remove(j);
                positions.getItems().remove(j);
            }
            rotorID.getItems().clear();
            positions.getItems().clear();
            ((VBox)(rotorsAndPositionsHBox.getChildren().get(i))).getChildren().clear();
        }
        rotorsAndPositionsHBox.getChildren().clear();

        SelectedReflectorComboBox.getItems().clear();

        VBox firstInputs=(VBox)PairsHBox.getChildren().get(0);
        VBox secondInputs=(VBox)PairsHBox.getChildren().get(1);
        for (int i = 0; i < firstInputs.getChildren().size(); i++) {
            ChoiceBox<Character> firstInputFromPair = ( ChoiceBox<Character>)firstInputs.getChildren().get(i);
            ChoiceBox<Character> secondInputFromPair = ( ChoiceBox<Character>)secondInputs.getChildren().get(i);
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
        CodeConfTabPane.getSelectionModel().select(0);
        setInitializeConfiguration();

        if(mEngine.isCodeConfigurationIsSet())
        {
            mEngine.resetAllData();
            mEngine.resetSelected();
            isSelected.set(false);
//
//            CurrentMachineCodeController.setSelectedCode(mEngine.getCodeFormat(true));
//            mainAppController.bindEncrypteCode();
//            CurrentCodeComponentController.SetCurrentCode(mEngine.getCodeFormat(true),false);
 //           mEngine.resetCodePosition();

            SelectedMachineCodeController.clearCurrentCodeView();
            CurrentMachineCodeController.clearCurrentCodeView();
        }


       // isSelected.set(false);
        isCodeSelectedByUser.set(false);
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsHBox.setDisable(toDisable);
        PairsHBox.setDisable(toDisable);
        SelectedReflectorComboBox.setDisable(toDisable);
        AddMorePairsButton.setDisable(toDisable);
        removePlugBoardPairButton.setDisable(toDisable);
    }

    public void ResetAllFieldsButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(false);
        resetAllFields();
    }

    public void addMorePairsButtonOnAction(ActionEvent actionEvent) {
        setPlugBoardPairs();
    }

    public void removePlugBoardPairOnAction(ActionEvent actionEvent) {
        if(numberOfPairs==0)
            removePlugBoardPairButton.setDisable(true);
        else
            removePlugBoardPairButton.setDisable(false);
        firstInputVBox.getChildren().remove(firstInputVBox.getChildren().size()-1);
        secondInputVBox.getChildren().remove(secondInputVBox.getChildren().size()-1);
        numberOfPairs--;
    }

    public void SelectedReflectorActionListener(ActionEvent actionEvent) {
        isCodeSelectedByUser.set(true);
       // isSelected.set(true);
        showCodeDetails.set(true);
    }

    public void updateCurrentCode() {
        CodeFormatDTO currentCode= CurrentMachineCodeController.getCurrentCode();
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
    }
}
