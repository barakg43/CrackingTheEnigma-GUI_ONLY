package UI.application.encryptTab;

import UI.application.AllMachineController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.statisticsComponent.StatisticsComponentController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.AtomicCounter;
import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.BruteForceLevel;
import dtoObjects.DmDTO.CandidateDTO;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.SimpleLongProperty;
import javafx.collections.transformation.FilteredList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.concurrent.atomic.AtomicLong;


public class EncryptTabController {

    @FXML public BorderPane mainPaneTab;
    public SplitPane encryptSplitPane;
    public ScrollPane currentCodeScrollPane;

    //Current Machine Configuration
    @FXML
    private HBox codeComponent;

    @FXML
    private SimpleCodeController codeComponentController;
    //Statistics Component
    @FXML
    private GridPane statisticsComponent;
    @FXML
    private StatisticsComponentController statisticsComponentController;

    // Encrypt\Decrypt Component
    @FXML
    private VBox encryptComponent;
    @FXML
    private EncryptComponentController encryptComponentController;

    private Engine enigmaEngine;
    private AllMachineController mainAppController;

    public Label testLabel;
    public ComboBox<BruteForceLevel> comboBoxBf;
    public TextField taskSizeField;
    private AtomicCounter counter;
    private DecryptionManager decryptionManager;
    SimpleLongProperty counterProperty;



    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }

    FilteredList<String> filteredData;

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {



//        statisticsComponent.prefHeightProperty().bind(heightProperty);
        mainPaneTab.prefWidthProperty().bind(widthProperty);
        mainPaneTab.prefHeightProperty().bind(heightProperty);
        statisticsComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());

//        codeComponent.prefHeightProperty().bind(mainPaneTab.heightProperty());


        codeComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());

        statisticsComponentController.bindSizePropertyToParent(mainPaneTab.widthProperty(),mainPaneTab.heightProperty());
    }
    public void bindTabDisable(SimpleBooleanProperty isCodeSelected)
    {
        encryptSplitPane.disableProperty().bind(isCodeSelected.not());
        currentCodeScrollPane.disableProperty().bind(isCodeSelected.not());
    }

    public SimpleCodeController getCodeComponentController() {
        return codeComponentController;
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode)
    {
        codeComponentController.setSelectedCode(currentCode);
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEncryptor(enigmaEngine);

      decryptionManager=new DecryptionManager(enigmaEngine);
        counterProperty=new SimpleLongProperty(0);
        testLabel.textProperty().bind(counterProperty.asString());
        // counterProperty=new SimpleLongProperty(counter,"counter");
        decryptionManager.addListenerTotalTaskDoneCounter(e -> {counterProperty.set((Long) e.getNewValue());
            System.out.println("counter is"+ e.getNewValue());
        });

    }
    public void doneProcessData()
    {
        statisticsComponentController.updateCodeStatisticsView(enigmaEngine.getStatisticDataDTO());

    }
    @FXML
    private void initialize() {

        encryptComponentController.setParentComponentTab(this);


        counter=new AtomicCounter();

        comboBoxBf.getItems().addAll(BruteForceLevel.values());

        //obsver=new ReadOnlyObjectWrapper<>(counter);
//        testLabel.textProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(final ObservableValue<? extends Number> observable,
//                                final Number oldValue, final Number newValue) {
//                if (counter.getAndSet(newValue.intValue()) == -1) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            testLabel.setText(String.valueOf(newValue));
//                        }
//                    });
//


    }

    public SimpleCodeController bindCodeComponentController()
    {
        return codeComponentController;
    }


    public void setMainAppController(AllMachineController mainController)
    {
        mainAppController=mainController;
    }

    public AllMachineController getMainController(){
        return mainAppController;
    }

    public void testBotton(ActionEvent ignoredActionEvent) {
//        System.out.println("Before  config"+enigmaEngine.getCodeFormat(true));
//        enigmaEngine.setCodeManually(enigmaEngine.getCodeFormat(true));
//        System.out.println("Before "+enigmaEngine.getCodeFormat(true));
//        String out = enigmaEngine.processDataInput(taskSizeField.getText());
//        enigmaEngine.setCodeManually(enigmaEngine.getCodeFormat(true));
//        System.out.println("after "+enigmaEngine.getCodeFormat(true));
//        String out2 = enigmaEngine.processDataInput(out);
//        String gyy;
//        if(enigmaEngine.getDictionary().checkIfAllLetterInDic(out2))
//            System.out.println("TRUE");
//        else
//            System.out.println("False");

        System.out.println("Starting BF!");

//        decryptionManager.setSetupConfiguration(comboBoxBf.getValue(),2,Integer.parseInt(taskSizeField.getText()));
//        decryptionManager.startBruteForce("aaaaa");
        counter.increment();

       // counterClass.setValue();
        decryptionManager.testCounter();
        System.out.println(counterProperty.get());
        //System.out.println(bindingCounter.get());
    }

    public void resumeOperation(ActionEvent ignoredActionEvent) {
        decryptionManager.resume();
    }

    public void pauseOperation(ActionEvent ignoredActionEvent) {
        decryptionManager.pause();
    }
}
