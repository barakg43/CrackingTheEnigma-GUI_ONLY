package UI.application.DmTab;

import UI.application.AllMachineController;
import UI.application.DmTab.DMencrypt.encryptTabDMController;
import UI.application.DmTab.Trie.Trie;
import decryptionManager.DecryptionManager;
import decryptionManager.components.Dictionary;
import enigmaEngine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DMcontroller {


    private DecryptionManager decryptionManager;
    Thread candidateListener;
    @FXML private VBox encryptComponent;
    @FXML private encryptTabDMController encryptComponentController;
     private ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
     private AllMachineController mainAppController;
     private Engine enigmaEngine;

    @FXML
    public void initialize()
    {
     //   decryptionManager=new DecryptionManager()
        if(encryptComponentController!=null)
        {
            encryptComponentController.setDMController(this);
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
        encryptComponentController.setEncryptor(enigmaEngine);
    }

    public AllMachineController getMainAppController()
    {
        return mainAppController;
    }
    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }
    public Dictionary getDictionary()
    {
        return decryptionManager.getDictionary();
    }

    public void setMainAppController(AllMachineController allMachineController) {
        mainAppController=allMachineController;
    }

}
