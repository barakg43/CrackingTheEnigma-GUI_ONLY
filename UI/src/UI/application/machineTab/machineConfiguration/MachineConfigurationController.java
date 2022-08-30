package UI.application.machineTab.machineConfiguration;

import UI.application.machineTab.currentCodeController.CurrentCodeController;
import UI.application.AllMachineController;
import UI.application.generalComponents.codeFormat.CodeController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
   @FXML public CurrentCodeController CurrentCodeComponentController;
    public Label CuurentMachineCode;
    public Label selectedMachineCodeLabel;
    public Label currentMachineLabel;
    public AnchorPane CurrentCodeConfigurationPane;
    public CodeController selectedCodeController;
    public Pane selectedCodePane;
    public TextField selectedRotors;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;

    private final ObservableList<Integer> rotorsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if(CurrentCodeComponentController!=null && selectedCodeController!=null)
        {
            CurrentCodeComponentController.SetMachineConfController(this);
            selectedCodeController.SetMachineConfController(this);
        }
    }


    public Engine getmEngine() {
        return mEngine;
    }
    public void setMainAppController(AllMachineController MainController) {
        mainAppController=MainController;
        mEngine=new EnigmaEngine();
    }
    public void setMachineDetails()
    {
        mEngine=mainAppController.getmEngine();
        machineData=mEngine.getMachineData();
        if(machineData!=null)
        {
            NumberOfRotors.setText(machineData.getNumberOfRotorsInUse()+"/" + machineData.getNumberOfRotorInSystem());
            numberOfReflectors.setText(String.valueOf(machineData.getNumberOfReflectors()));
            CipheredInputs.setText(String.valueOf(mEngine.getCipheredInputs()));
            MachineDetails.setVisible(true);
        }
        if(mEngine.isCodeConfigurationIsSet())
        {
            CodeFormatDTO selectedCode=mEngine.getCodeFormat(true);
            SelectedMachineCode.setText(selectedCode.toString());
            CodeFormatDTO currentCode=mEngine.getCodeFormat(false);
            CuurentMachineCode.setText(currentCode.toString());
            setVisibleCodeFields(true);
        }
        else{
            setVisibleCodeFields(false);
        }
    }

    private void setVisibleCodeFields(boolean toVisible)
    {
        SelectedMachineCode.setVisible(toVisible);
        CuurentMachineCode.setVisible(toVisible);
        selectedMachineCodeLabel.setVisible(toVisible);
        currentMachineLabel.setVisible(toVisible);
    }

    public void setInitializeConfiguration()
    {
        MachineCodePane.setVisible(true);
//        for(int i=0; i< machineData.getRotorsId().length ;i++)
//        {
//            rotorsList.add(machineData.getRotorsId()[i]);
//        }
        selectedRotors.setText("");
        SelectRotorsLabel.setText("Select " + machineData.getNumberOfRotorsInUse() + " rotors (between 1 to " + machineData.getRotorsId().length
        + "):");
      //  RotorsListView.setItems(rotorsList);
       // RotorsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<String> reflectorIDName=machineData.getReflectorIdList();
        ObservableList<String> reflectorID= FXCollections.observableArrayList();
        for(String ref:reflectorIDName) {
            reflectorID.add(ref);
        }
        SelectedReflectorComboBox.setItems(reflectorID);
    }

   @FXML
    public void SetCodeConfActionListener(javafx.event.ActionEvent actionEvent) {

        selectedCodeController.resetTextFlow();
//        ObservableList selectedIndices =
//                RotorsListView.getSelectionModel().getSelectedItems();
       String rotors=selectedRotors.getText();


       // List<String> rotorsListstr=new ArrayList<>();
//        for(int i=0;i<selectedIndices.size();i++)
//        {
//            rotorsListstr.add(String.valueOf(selectedIndices.get(i)));
//        }

        mEngine.checkIfRotorsValid(rotors);

        String positions=InitialRotorsPositionsTextField.getText();
        mEngine.checkIfPositionsValid(positions);

        String selectedReflector=(String) SelectedReflectorComboBox.getValue();
        mEngine.setReflector(selectedReflector);

       boolean withPlugBoardPairs=WithPlugBoardPairs.isSelected();
       if(withPlugBoardPairs)
       {
           String allText = PlugBoardPairsTextArea.getText().replaceAll("\n", "");
           mEngine.checkPlugBoardPairs(allText);
       }
       else{
           mEngine.checkPlugBoardPairs("");
       }

       CodeFormatDTO selectedCode=mEngine.getCodeFormat(true);

       selectedCodeController.setSelectedCode(selectedCode.toString());
       SelectedMachineCode.setText(selectedCode.toString());

       CodeFormatDTO currentCode=mEngine.getCodeFormat(false);
       CuurentMachineCode.setText(currentCode.toString());
       setVisibleCodeFields(true);

       CurrentCodeConfigurationPane.setVisible(true);
       CurrentCodeComponentController.SetCurrentCode(mEngine);
   }


    private List<String> rotorsConfig(){

//        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredRotors.setPredicate(employee -> {
//                // If filter text is empty, display all persons.
//
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                // Compare first name and last name of every person with filter text.
//                String rotor = newValue;
//
//                if (Arrays.asList(machineData.getRotorsId()).contains(Integer.parseInt(rotor)))
//                    return true;
//                return false;
//            });
//        });

        // 3. Wrap the FilteredList in a SortedList.

        FilteredList<Integer> filteredRotors = new FilteredList<>(rotorsList,b->true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRotors.setPredicate(searchRotor -> {
                if(newValue.isEmpty() || newValue== null )
                    return true;
                 int searchRotorID=Integer.parseInt(newValue);
                if(Collections.singletonList(machineData.getRotorsId()).contains(searchRotorID))
                    return true;
                return false;
            });
        });

        SortedList<Integer> sortedRotors= new SortedList<>(filteredRotors);
        RotorsListView.setItems(sortedRotors);

        ObservableList selectedIndices =
                RotorsListView.getSelectionModel().getSelectedIndices();
        List<String> rotorsListstr=new ArrayList<>();
        for(int i=0;i<selectedIndices.size();i++)
        {
            rotorsListstr.add(String.valueOf(selectedIndices.get(i)));
        }
        return rotorsListstr;

    }

    public void WithPlugBoardCheckedAction(javafx.event.ActionEvent actionEvent) {

        writePlugBoardLabel.setVisible(WithPlugBoardPairs.isSelected());
        PlugBoardPairsTextArea.setVisible(WithPlugBoardPairs.isSelected());
        if(WithPlugBoardPairs.isSelected())
        {
            ButtonsPane.setLayoutX(writePlugBoardLabel.getLayoutX());
            ButtonsPane.setLayoutY(PlugBoardPairsTextArea.getLayoutY() +70);
            SelectedLabel.setLayoutX(ButtonsPane.getLayoutX());
            SelectedLabel.setLayoutY(ButtonsPane.getLayoutY()+70);
            selectedCodePane.setLayoutX(SelectedLabel.getLayoutX()+200);
            selectedCodePane.setLayoutY(SelectedLabel.getLayoutY());
        }
        else{
            ButtonsPane.setLayoutX(writePlugBoardLabel.getLayoutX());
            ButtonsPane.setLayoutY(PlugBoardPairsTextArea.getLayoutY() -40);
            SelectedLabel.setLayoutX(ButtonsPane.getLayoutX());
            SelectedLabel.setLayoutY(ButtonsPane.getLayoutY()+80);
            selectedCodePane.setLayoutX(SelectedLabel.getLayoutX()+200);
            selectedCodePane.setLayoutY(SelectedLabel.getLayoutY());

        }
    }


    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        resetAllFields();
        disableAllFields(true);
        mEngine.resetSelected();
        mEngine.setCodeAutomatically();
        CodeFormatDTO selectedCode=mEngine.getCodeFormat(true);
        selectedCodeController.setSelectedCode(selectedCode.toString());
        SelectedMachineCode.setText(selectedCode.toString());
        CuurentMachineCode.setText(mEngine.getCodeFormat(false).toString());
        CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(mEngine);
        //disableAllFields(false);

    }

    public void resetAllFields()
    {
//        searchTextField.clear();
//        RotorsListView.getItems().clear();
//        for(int i=0; i< machineData.getRotorsId().length ;i++)
//            rotorsList.add(machineData.getRotorsId()[i]);
//        RotorsListView.setItems(rotorsList);
        selectedRotors.setText("");
        InitialRotorsPositionsTextField.clear();
        SelectedReflectorComboBox.valueProperty().set(null);
        PlugBoardPairsTextArea.clear();
        WithPlugBoardPairs.setSelected(false);
        PlugBoardPairsTextArea.setVisible(false);
        writePlugBoardLabel.setVisible(false);
        CurrentCodeComponentController.resetFields();
        CuurentMachineCode.setText("");
        SelectedMachineCode.setText("");
        selectedCodeController.resetTextFlow();

    }

    private void disableAllFields(boolean toDisable)
    {
        selectedRotors.setDisable(toDisable);
        InitialRotorsPositionsTextField.setDisable(toDisable);
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
}
