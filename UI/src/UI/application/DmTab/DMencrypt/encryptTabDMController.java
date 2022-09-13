package UI.application.DmTab.DMencrypt;

import UI.application.AllMachineController;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.Trie.Trie;
import UI.application.DmTab.Trie.TrieNode;
import UI.application.encryptTab.EncryptTabController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import UI.application.encryptTab.encryptComponent.manualEncrypt.ManualEncryptController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import enigmaEngine.Encryptor;
import enigmaEngine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class encryptTabDMController {

    public TextField searchBox;

    public Button searchButton;
    public Button deleteButton;
    public ListView dictionaryListView;
    public VBox automaticEncrypteComponent;
    private automaticEncrypteDMController automaticEncrypteComponentController;
    @FXML
    private HBox simpleCodeComponent;

    @FXML
    private SimpleCodeController simpleCodeComponentController;
    Trie dictionaryTrie;
    private Encryptor encryptor;
   private DMcontroller DmController;

    private  ObservableList<String> dictionaryWords = FXCollections.observableArrayList();

    private Engine enigmaEngine;
    private AllMachineController mainAppController;

    @FXML
    private void initialize() {

        if(automaticEncrypteComponentController!=null)
        {
            automaticEncrypteComponentController.setParentComponentTab(this);
        }
        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
    }

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
        automaticEncrypteComponentController.setEncryptor(encryptor);
    }

    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }


    public void doneProcessData()
    {
        simpleCodeComponentController.setSelectedCode(DmController.getEnigmaEngine().getCodeFormat(false));
        DmController.getMainAppController().bindCurrentCode();
    }


    public void setDictionaryTrie() {
        dictionaryTrie=new Trie();
    }
    public void search(String oldVal, String newVal) {
        ObservableList<String> subentries = FXCollections.observableArrayList();

        if(newVal.isEmpty())
        {
            DmController.setDictionaryList();
        }
        else{
            TrieNode node = dictionaryTrie.searchNode(newVal);
            if(node!=null)
            {
                dictionaryTrie.wordsFinderTraversal(node,0);
                ArrayList<String> prefixWords= dictionaryTrie.getWordsArray();

                for (String word : prefixWords) {
                    subentries.add(word);
                }

            }
            dictionaryListView.setItems(subentries);

        }

    }

    public ListView getDictionaryListView()
    {
        return dictionaryListView;
    }
    public Trie getTrieDictionary(){return dictionaryTrie;}
    public void setDMController(DMcontroller DMController) {

        this.DmController=DMController;
    }

    public void deleteButtonOnAction(ActionEvent actionEvent) {
        searchBox.clear();

    }

    public void setMainAppController(AllMachineController mainController)
    {
        mainAppController=mainController;
    }

    public AllMachineController getMainController(){
        return mainAppController;
    }


    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        automaticEncrypteComponentController.setEncryptor(enigmaEngine);
    }

    public SimpleCodeController getCodeComponentController() {
      return simpleCodeComponentController;
    }
}
