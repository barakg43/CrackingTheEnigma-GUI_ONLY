package UI.application.MachineConfTab;


import UI.application.AllMachineController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

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

    private Set<Integer> rotorIDSet;
    private Set<Character> alphabetSet;
    List<ComboBox<Integer>> rotorIDGroupedComboBoxes;
    List<ComboBox<Character>> plugBoardGroupedComboBoxes;
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
        removePlugBoardPairButton.setDisable(true);
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
        CurrentMachineCodeController.clearCurrentCodeView();
        mEngine = mainAppController.getmEngine();
        machineData = mEngine.getMachineData();
        createAlphabetRotorIdSet();
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

    private void createAlphabetRotorIdSet() {



        rotorIDSet=Arrays.stream(machineData.getRotorsId())
                .boxed().
                collect(Collectors.toSet());
        alphabetSet = machineData.getAlphabetString()
                .chars().
                mapToObj(c -> (Character)(char)c)
                .collect(Collectors.toSet());

        rotorIDGroupedComboBoxes=new ArrayList<>();
        plugBoardGroupedComboBoxes=new ArrayList<>();

    }


    private void setVisibleCodeFields(boolean toVisible) {
        SelectedMachineCode.setVisible(toVisible);
    }

    public void setInitializeConfiguration() {

        firstInputVBox.getChildren().clear();
        secondInputVBox.getChildren().clear();
        isCodeSelectedByUser.set(false);
        MachineCodePane.setVisible(true);
        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
//        int[] rotorsId = machineData.getRotorsId();
//        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            createComboBox();
        }
        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorID = FXCollections.observableArrayList();
        for (String ref : reflectorIDName) {
            reflectorID.add(ref);
        }
        SelectedReflectorComboBox.setItems(reflectorID);
    }


    private void createComboBox() {
        VBox Vbox = new VBox();
        Vbox.getChildren().add(SetRotorComboBox());
        Vbox.getChildren().add(SetPositionsComboBox());
        Vbox.setSpacing(30);
        rotorsAndPositionsHBox.getChildren().add(Vbox);
        rotorsAndPositionsHBox.setSpacing(40);
    }

    private ComboBox<Integer> SetRotorComboBox() {
        ObservableList<Integer> rotorsIDList = FXCollections.observableArrayList(rotorIDSet);
        //rotorsIDList.addAll(rotorIDSet);
        ComboBox<Integer> rotorsComboBox = new ComboBox<>(rotorsIDList);
        rotorIDGroupedComboBoxes.add(rotorsComboBox);
        rotorsComboBox.setVisibleRowCount(10);
        rotorsComboBox.setMinWidth(20);
        rotorsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");

        //remove selected id value from other rotor id combobox
        rotorsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            rotorIDGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(rotorsComboBox))).forEach(comboBox ->
            {
                comboBox.getItems().remove(newValue);
                if (oldValue != null && !comboBox.getItems().contains(oldValue))
                    comboBox.getItems().add(oldValue);
                FXCollections.sort(comboBox.getItems());

            });
        });
//        for (int i = 0; i < rotorsID.length; i++) {
//            rotorsIDList.add(rotorsID[i]);
//        }
//        rotorsComboBox.getItems().addAll(rotorsIDList);
       // return rotorsComboBox;
        return rotorsComboBox;
    }

    private ComboBox <Character> SetPositionsComboBox() {
        ObservableList<Character> positionsList = FXCollections.observableArrayList(alphabetSet);
        ComboBox <Character> positionsComboBox = new ComboBox <>(positionsList);
        positionsComboBox.setVisibleRowCount(10);
        positionsComboBox.setMinWidth(20);
        positionsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");

//        for (int i = 0; i < positions.length(); i++) {
//            positionsList.add(positions.charAt(i));
//        }
//        positionsComboBox.getItems().addAll(positionsList);
//        return positionsComboBox;
        return positionsComboBox;
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
                if(((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                int selectedID=((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                Character selectedPosition=((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem();

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


            for(int i=0;i<firstInputVBox.getChildren().size();i++)
            {
                if(((ComboBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character firstInput = ((ComboBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                if(((ComboBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character secondInput=((ComboBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
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

    private ComboBox<Character> SetPairsComboBox() {
        ComboBox<Character> pairComboBox = new ComboBox<>();
//        ObservableList<Character> alphabetList = FXCollections.observableArrayList();
//        for (int i = 0; i < alphabet.length(); i++) {
//            alphabetList.add(alphabet.charAt(i));
//        }
        plugBoardGroupedComboBoxes.add(pairComboBox);
        pairComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            plugBoardGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(pairComboBox))).forEach(comboBox ->
            {
                comboBox.getItems().remove(newValue);
                if (oldValue != null && !comboBox.getItems().contains(oldValue))
                    comboBox.getItems().add(oldValue);
                FXCollections.sort(comboBox.getItems());

            });
        });
        pairComboBox.setPrefSize(70, 10);
        pairComboBox.getItems().addAll(alphabetSet);
        //remove selected id value from other rotor id combobox

        return pairComboBox;

    }

    private void setPlugBoardPairs()
    {
        removePlugBoardPairButton.setDisable(false);
        String alphabet=machineData.getAlphabetString();
        firstInputVBox.getChildren().add(SetPairsComboBox());
        secondInputVBox.getChildren().add(SetPairsComboBox());
        PairsHBox.setSpacing(20);
        firstInputVBox.setSpacing(10);
        secondInputVBox.setSpacing(10);

        if(firstInputVBox.getChildren().size()==alphabet.length()/2)
            AddMorePairsButton.setDisable(true);
        else
            AddMorePairsButton.setDisable(false);
    }

    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
   //    resetAllFields();
        disableAllFields(true);
  //      mEngine.resetSelected();
        mEngine.setCodeAutomatically();
        showCodeDetails.set(true);
        isSelected.set(true);
      //  CurrentMachineCodeController.clearCurrentCodeView();
      //  SelectedMachineCodeController.clearCurrentCodeView();
        showAllCodes();
        disableAllFields(false);

//        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[3];
//        List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
//        rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
//        rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
//        rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
//        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",plugboardPairDTOList);

//        System.out.println("starting brute force");
//        //mainAppController.startBF(codeFormatDTO, BruteForceLevel.easyLevel);
//        System.out.println("After brute force");
    }

    public void resetAllFields()
    {
        for (int i = 0; i <  rotorsAndPositionsHBox.getChildren().size(); i++) {
           VBox rotorAndPositionVBOx= (VBox)(rotorsAndPositionsHBox.getChildren().get(i));
           ComboBox<Integer> rotorID = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(0));
            ComboBox<Integer> positions = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(1));
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
            ComboBox<Character> firstInputFromPair = (ComboBox<Character>)firstInputs.getChildren().get(i);
            ComboBox<Character> secondInputFromPair = (ComboBox<Character>)secondInputs.getChildren().get(i);
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
        firstInputVBox.getChildren().remove(firstInputVBox.getChildren().size()-1);
        secondInputVBox.getChildren().remove(secondInputVBox.getChildren().size()-1);
        if(firstInputVBox.getChildren().size()==0) {
            removePlugBoardPairButton.setDisable(true);

        }
        else
        {
            removePlugBoardPairButton.setDisable(false);

        }
        AddMorePairsButton.setDisable(false);
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
