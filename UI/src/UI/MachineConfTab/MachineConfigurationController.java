package UI.MachineConfTab;

import UI.AllMachine.AllMachineController;
import UI.Code.CodeController;
import UI.CurrentCode.CurrentCodeController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class MachineConfigurationController {

    //Machine Details
    public AnchorPane MachineDetails;
    public Label NumberOfRotors;
    public Label numberOfReflectors;
    public Label CipheredInputs;
    public Pane MachineCodePane;

    //Machine code configuration - initialize
    public Label SelectRotorsLabel;
    public TextField searchTextField;
    public ListView RotorsListView;
    public TextField InitialRotorsPositionsTextField;
    public ComboBox SelectedReflectorComboBox;
    public CheckBox WithPlugBoardPairs;
    public TextArea PlugBoardPairsTextArea;
    public Label writePlugBoardLabel;
    public Button SetCodeConfButton;
    public Label SelectedMachineCode;
    public Pane ButtonsPane;
    public Label SelectedLabel;
    @FXML
    public CurrentCodeController CurrentCodeComponentController;
    public Label CuurentMachineCode;
    public Label selectedMachineCodeLabel;
    public Label currentMachineLabel;
    public AnchorPane CurrentCodeConfigurationPane;
    public CodeController CodeController;
    public Pane selectedCodePane;
    public TextField selectedRotors;
    public FlowPane rotorsFlowPane;
    public HBox rotorsAndPositionsHBox;
    public Tab rotorsAndPositionsTab;

    // Tab: plugBoard pairs
    public HBox PairsHBox;
    public VBox firstInputVBox;
    public VBox secondInputVBox;
    public Button AddMorePairsButton;
    public Label configFirstLabel;
    public Button ResetAllFieldsButton;
    public Button GetRandomButton;
    public AnchorPane selectedCodeAPane;
    public CurrentCodeController SelectedCodeComponentController;
    public TabPane CodeConfTabPane;
    public Label currentConfigurationLabel;
    public ScrollPane selectedCodeScrollPane;
    public ScrollPane selectedCodeConfiguration;
    public Label errorLabel;
    public Label errorTextLabel;


    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;
    private int numberOfPairs;

    @FXML
    public void initialize() {


        if (CurrentCodeComponentController != null && CodeController != null && SelectedCodeComponentController!=null) {
            CurrentCodeComponentController.SetMachineConfController(this);
            CodeController.SetMachineConfController(this);
            SelectedCodeComponentController.SetMachineConfController(this);
        }
    }
    public MachineConfigurationController()
    {
        numberOfPairs=0;
    }

    public Engine getmEngine() {
        return mEngine;
    }

    public void setMainAppController(AllMachineController MainController) {
        mainAppController = MainController;
        mEngine = new EnigmaEngine();
    }

    public void setMachineDetails() {
        mEngine = mainAppController.getmEngine();
        machineData = mEngine.getMachineData();
        if (machineData != null) {
            NumberOfRotors.setText(machineData.getNumberOfRotorsInUse() + "/" + machineData.getNumberOfRotorInSystem());
            numberOfReflectors.setText(String.valueOf(machineData.getNumberOfReflectors()));
            CipheredInputs.setText(String.valueOf(mEngine.getCipheredInputs()));
            MachineDetails.setVisible(true);
        }
        if (mEngine.isCodeConfigurationIsSet()) {
            CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);
            SelectedMachineCode.setText(selectedCode.toString());
            CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
            CuurentMachineCode.setText(currentCode.toString());
            setVisibleCodeFields(true);
        } else {
            setVisibleCodeFields(false);
        }
    }

    private void setVisibleCodeFields(boolean toVisible) {
        SelectedMachineCode.setVisible(toVisible);
        CuurentMachineCode.setVisible(toVisible);
        selectedMachineCodeLabel.setVisible(toVisible);
        currentMachineLabel.setVisible(toVisible);
    }

    public void setInitializeConfiguration() {
        MachineCodePane.setVisible(true);
        selectedCodeConfiguration.setVisible(true);
        selectedCodeScrollPane.setVisible(false);
        currentConfigurationLabel.setVisible(false);
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

        List<Integer> selectedRotorsID = new ArrayList<>();
        List<Character> selectedPositions =new ArrayList<>();

        for (int i = 0; i < machineData.getNumberOfRotorsInUse(); i++) {
            VBox rotorAndPosition=(VBox) rotorsAndPositionsHBox.getChildren().get(i);
            selectedRotorsID.add(((ChoiceBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem());
            selectedPositions.add(((ChoiceBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem());
        }
        try {
            mEngine.checkIfRotorsValid(selectedRotorsID);
        }catch (Exception ex)
        {
            errorLabel.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText(ex.getMessage());
        }

        mEngine.checkIfPositionsValid(selectedPositions);

        String selectedReflector = (String) SelectedReflectorComboBox.getValue();
        mEngine.setReflector(selectedReflector);

        boolean withPlugBoardPairs = WithPlugBoardPairs.isSelected();
        List<PlugboardPairDTO> plugBoardPairs=new ArrayList<>();
        if (withPlugBoardPairs) {

            for(int i=0;i<numberOfPairs;i++)
            {
                Character firstInput = ((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                Character secondInput=((ChoiceBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                plugBoardPairs.add(new PlugboardPairDTO(firstInput,secondInput));
            }

            mEngine.setPlugBoardPairs(plugBoardPairs);
        } else {
            mEngine.setPlugBoardPairs(plugBoardPairs);
        }

        showAllCodes();

    }

    private void showAllCodes()
    {
        selectedCodeScrollPane.setVisible(true);
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);
        configFirstLabel.setText("Selected Code");
        currentConfigurationLabel.setVisible(true);
        configFirstLabel.setVisible(false);

        SelectedCodeComponentController.SetCurrentCode(selectedCode);
        SelectedMachineCode.setText(selectedCode.toString());
        CodeConfTabPane.getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        CuurentMachineCode.setText(currentCode.toString());
        setVisibleCodeFields(true);
        CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(currentCode);

        ResetAllFieldsButton.setDisable(false);
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
    }
    public void WithPlugBoardCheckedAction(javafx.event.ActionEvent actionEvent) {

        setPlugBoardPairs();
    }


    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        resetAllFields();
        disableAllFields(true);
        mEngine.resetSelected();
        mEngine.setCodeAutomatically();

        showAllCodes();
    }

    public void resetAllFields()
    {
        rotorsAndPositionsHBox.getChildren().clear();
        SelectedReflectorComboBox.valueProperty().set(null);
        WithPlugBoardPairs.setSelected(false);
        PairsHBox.getChildren().clear();
        CurrentCodeComponentController.resetFields();
        SelectedCodeComponentController.resetFields();
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsHBox.setDisable(toDisable);
        PairsHBox.setDisable(toDisable);
        SelectedReflectorComboBox.setDisable(toDisable);
        WithPlugBoardPairs.setDisable(toDisable);
    }

    public void ResetAllFieldsButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(false);
        resetAllFields();
    }

    public void SelecteReflectorActionListener(ActionEvent actionEvent) {
        SetCodeConfButton.setDisable(false);
    }

    public void addMorePairsButtonOnAction(ActionEvent actionEvent) {
        setPlugBoardPairs();
    }
}
