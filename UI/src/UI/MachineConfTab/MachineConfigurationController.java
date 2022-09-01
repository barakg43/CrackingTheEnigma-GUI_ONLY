package UI.MachineConfTab;

import UI.AllMachine.AllMachineController;
import UI.SimpleCode.SimpleCodeController;
import UI.NewCodeFormat.NewCodeFormatController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
    public ComboBox SelectedReflectorComboBox;
    public CheckBox WithPlugBoardPairs;
    public Button SetCodeConfButton;
    public Label SelectedMachineCode;
    public NewCodeFormatController CurrentCodeComponentController;
    public Label CuurentMachineCode;
    public Label selectedMachineCodeLabel;
    public Label currentMachineLabel;
    public AnchorPane CurrentCodeConfigurationPane;
    public SimpleCodeController CodeController;
    public HBox rotorsAndPositionsHBox;
    // Tab: plugBoard pairs
    public HBox PairsHBox;
    public VBox firstInputVBox;
    public VBox secondInputVBox;
    public Button AddMorePairsButton;
    public Label configFirstLabel;
    public NewCodeFormatController SelectedCodeComponentController;
    public TabPane CodeConfTabPane;
    public Label currentConfigurationLabel;
    public ScrollPane selectedCodeScrollPane;
    public ScrollPane selectedCodeConfiguration;
    public Label errorLabel;

    public Label ErrorTextLabel;
    public Button removePlugBoardPairButton;
    public Button ResetAllFieldsButton;


    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;
    private int numberOfPairs;

    private SimpleBooleanProperty isCodeSelectedByUser;
    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty isCodeError;
    private SimpleStringProperty errorText;



    @FXML
    public void initialize() {

        if (CurrentCodeComponentController != null && CodeController != null && SelectedCodeComponentController!=null) {
            CurrentCodeComponentController.SetMachineConfController(this);
            CodeController.SetMachineConfController(this);
            SelectedCodeComponentController.SetMachineConfController(this);
        }

        ErrorTextLabel.visibleProperty().bind(isCodeError);
        errorLabel.visibleProperty().bind(isCodeError);
        ErrorTextLabel.textProperty().bind(errorText);
        ResetAllFieldsButton.disableProperty().bind(isSelected.not());
        SetCodeConfButton.disableProperty().bind(isCodeSelectedByUser.not());

    }
    public MachineConfigurationController()
    {
        numberOfPairs=0;
        isCodeSelectedByUser=new SimpleBooleanProperty();
        isSelected=new SimpleBooleanProperty();
        errorText=new SimpleStringProperty();
        isCodeError=new SimpleBooleanProperty(false);
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

        numberOfPairs=0;
        isCodeSelectedByUser.set(false);
        currentConfigurationLabel.setVisible(false);
        configFirstLabel.setVisible(true);
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

        isCodeError.set(false);

        List<Integer> selectedRotorsID = new ArrayList<>();
        List<Character> selectedPositions =new ArrayList<>();
        try {
            for (int i = 0; i < rotorsAndPositionsHBox.getChildren().size(); i++) {
                VBox rotorAndPosition=(VBox) rotorsAndPositionsHBox.getChildren().get(i);
                if(rotorAndPosition==null)
                    throw new Exception("You need to configurate all data.");
                if(((ChoiceBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions. Please check column number: " + (i+1));
                int selectedID=((ChoiceBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ChoiceBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions. Please check column number: " + (i+1));
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

            boolean withPlugBoardPairs = WithPlugBoardPairs.isSelected();
            List<PlugboardPairDTO> plugBoardPairs=new ArrayList<>();
            if (withPlugBoardPairs) {

                for(int i=0;i<numberOfPairs;i++)
                {
                    if(((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                        throw new Exception("You need to select pairs. Please check pair number: " + (i+1));
                    Character firstInput = ((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                    if(((ChoiceBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedIndex()==-1)
                        throw new Exception("You need to select pairs. Please check pair number: " + (i+1));
                    Character secondInput=((ChoiceBox<Character>)(secondInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                    plugBoardPairs.add(new PlugboardPairDTO(firstInput,secondInput));
                }
                mEngine.checkPlugBoardPairs(plugBoardPairs);
            } else {
                mEngine.checkPlugBoardPairs(plugBoardPairs);
            }

            showAllCodes();
            isCodeSelectedByUser.set(false);

        }catch (Exception ex)
        {
            configFirstLabel.setVisible(false);
            currentConfigurationLabel.setVisible(false);
            isCodeError.set(true);
            errorText.set(ex.getMessage());
            CodeConfTabPane.getSelectionModel().select(0);
           // resetAllFields();
        }


    }

    private void showAllCodes()
    {
        selectedCodeScrollPane.setVisible(true);
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);
       // configFirstLabel.setText("Selected Code");
        currentConfigurationLabel.setVisible(true);
        configFirstLabel.setVisible(false);

        SelectedCodeComponentController.SetCurrentCode(selectedCode,false);
        SelectedMachineCode.setText(selectedCode.toString());
        CodeConfTabPane.getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        CuurentMachineCode.setText(currentCode.toString());
        setVisibleCodeFields(true);
        CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);

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

        if(WithPlugBoardPairs.isSelected())
              setPlugBoardPairs();
    }


    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        isCodeError.set(false);
        resetAllFields();
        disableAllFields(true);
        mEngine.resetSelected();
        mEngine.setCodeAutomatically();
        isSelected.set(true);
        showAllCodes();
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
        WithPlugBoardPairs.setSelected(false);

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
       // PairsHBox.getChildren().clear();

        CurrentCodeComponentController.resetFields();
        SelectedCodeComponentController.resetFields();
        CodeConfTabPane.getSelectionModel().select(0);
        setInitializeConfiguration();
        isSelected.set(false);
        isCodeSelectedByUser.set(false);
        isCodeError.set(false);
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsHBox.setDisable(toDisable);
        PairsHBox.setDisable(toDisable);
        SelectedReflectorComboBox.setDisable(toDisable);
        WithPlugBoardPairs.setDisable(toDisable);
        AddMorePairsButton.setDisable(toDisable);
        removePlugBoardPairButton.setDisable(toDisable);
    }

    public void ResetAllFieldsButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(false);
        resetAllFields();
    }

    public void SelecteReflectorActionListener(ActionEvent actionEvent) {
        isCodeSelectedByUser.set(true);
        isSelected.set(true);
    }

    public void addMorePairsButtonOnAction(ActionEvent actionEvent) {
        setPlugBoardPairs();
    }

    public void removePlugBoardPairOnAction(ActionEvent actionEvent) {
        firstInputVBox.getChildren().remove(firstInputVBox.getChildren().size()-1);
        secondInputVBox.getChildren().remove(secondInputVBox.getChildren().size()-1);
        numberOfPairs--;
    }
}
