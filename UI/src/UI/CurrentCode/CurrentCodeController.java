package UI.CurrentCode;

import UI.Code.CodeController;
import UI.MachineConfTab.MachineConfigurationController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.RotorInfoDTO;
import enigmaEngine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

public class CurrentCodeController {

    public TableColumn RotorIDColumn;
    public TableColumn PositionColumn;
    public Label NoPlubBoardPairsLabel;
    public Pane CurrentCodePane;
    public Label rotorsCurrCode;
    public Label positionCurrLabel;
    public TextFlow currCode;
    public CodeController currCodeController;
    public Pane currentCodePane;
    private MachineConfigurationController machineConfController;
    @FXML
    private Label SelectedReflectorLabel;

    @FXML
    private Label SelectedReflector;

    @FXML
    private Label RotorsAndPositionsLabel;

    @FXML
    private TableView<rotorsAndPositions> RotorsAndPositionsTable;

    @FXML
    private ListView<PlugboardPairDTO> PairsListView;

    @FXML
    private Label PlugBoardPairsLabel;

    @FXML
    private Label CurrentCodeLabel;

    @FXML
    public void initialize() {
        if (currCodeController != null) {
            currCodeController.setCurrCodeController(this);
        }
    }

    public void SetMachineConfController(MachineConfigurationController machineConfigurationController) {
        machineConfController=machineConfigurationController;
    }

    public void SetCurrentCode(CodeFormatDTO currentCode) {

        currCodeController.resetTextFlow();

        SelectedReflector.setText(currentCode.getReflectorID());

        RotorInfoDTO[] SelectedRotors=currentCode.getRotorInfo();
        ObservableList<rotorsAndPositions> rotorsAndPositions = FXCollections.observableArrayList();
        for (RotorInfoDTO rotor:SelectedRotors) {
            rotorsAndPositions.add(new rotorsAndPositions(rotor.getId(),String.valueOf(rotor.getStatingLetter())));
        }

        RotorIDColumn.setCellValueFactory(new PropertyValueFactory<rotorsAndPositions,Integer>("rotorID"));
        PositionColumn.setCellValueFactory(new PropertyValueFactory<rotorsAndPositions,String>("rotorCurrPosition"));
        RotorsAndPositionsTable.setItems(rotorsAndPositions);

        ObservableList<PlugboardPairDTO> plugBoardPairs = FXCollections.observableArrayList();
        if(currentCode.getPlugboardPairDTOList().size()!=0) {
            for (int i = 0; i < currentCode.getPlugboardPairDTOList().size(); i++) {
                plugBoardPairs.add(currentCode.getPlugboardPairDTOList().get(i));
            }

            PairsListView.setItems(plugBoardPairs);
            PairsListView.setVisible(true);
            NoPlubBoardPairsLabel.setVisible(false);
            //CurrentCodePane.setLayoutX(PlugBoardPairsLabel.getLayoutX());
            //CurrentCodePane.setLayoutY(PlugBoardPairsLabel.getLayoutY()+100);

        }
        else{
            NoPlubBoardPairsLabel.setVisible(true);
            PairsListView.setVisible(false);
            //CurrentCodePane.setLayoutX(PlugBoardPairsLabel.getLayoutX());
           // CurrentCodePane.setLayoutY(PlugBoardPairsLabel.getLayoutY()+30);
        }

        currCodeController.setSelectedCode(currentCode.toString());
    }

    public void resetFields()
    {
        SelectedReflector.setText("");
        RotorsAndPositionsTable.getItems().clear();
        PairsListView.getItems().clear();
        currCodeController.resetTextFlow();
    }
}
