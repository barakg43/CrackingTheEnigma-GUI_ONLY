package UI.application.DmTab;

import UI.application.AllMachineController;
import UI.application.DmTab.DMTaskComponents.TaskDataController;
import UI.application.DmTab.DMTaskComponents.taskProgressController;
import UI.application.DmTab.DMencrypt.encryptTabDMController;
import UI.application.DmTab.DMoperational.DMoperationalController;
import UI.application.DmTab.Trie.Trie;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.Dictionary;
import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DMcontroller {


    @FXML public VBox taskDataComponent;

    @FXML private VBox dmVBox;
    private DecryptionManager decryptionManager;
    @FXML private GridPane operationalComponent;
    Thread candidateListener;
    @FXML private VBox encryptComponent;
    @FXML private encryptTabDMController encryptComponentController;
    @FXML private TaskDataController taskDataComponentController;
    @FXML private DMoperationalController operationalComponentController;

     private ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
     private AllMachineController mainAppController;
     private Engine enigmaEngine;

    @FXML
    public void initialize()
    {
     //   decryptionManager=new DecryptionManager()
        if(encryptComponentController!=null && taskDataComponentController!=null && operationalComponentController!=null)
        {
            encryptComponentController.setDMController(this);
            operationalComponentController.setDMControoler(this);
            taskDataComponentController.setDMControoler(this);
        }
        candidateListener=new Thread("candidate taker");

    }

    public void setDictionaryList()
    {
        dictionaryWords.addAll(enigmaEngine.getDictionary().getWordsSet());
        encryptComponentController.getDictionaryListView().setItems(dictionaryWords);
        encryptComponentController.setDictionaryTrie();
        Trie trieDic = encryptComponentController.getTrieDictionary();
        for (String word:enigmaEngine.getDictionary().getWordsSet()) {
            trieDic.insert(word);
        }
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEnigmaEngine(enigmaEngine);
    }

    public AllMachineController getMainAppController()
    {
        return mainAppController;
    }
    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }


    public void setMainAppController(AllMachineController allMachineController) {
        mainAppController=allMachineController;
    }

    public void setSimpleCurrCode(CodeFormatDTO currentCode) {
        encryptComponentController.setSelectedCode(currentCode);
    }

    public void bindTabDisable(SimpleBooleanProperty isSelected) {

        dmVBox.disableProperty().bind(isSelected.not());
        encryptComponentController.setDisableBind(isSelected);
    }

    public SimpleCodeController bindCodeComponentController() {
        return encryptComponentController.getCodeComponentController();
    }

    public void addCandidates() {
        taskDataComponentController.addCandidates();
    }
}
