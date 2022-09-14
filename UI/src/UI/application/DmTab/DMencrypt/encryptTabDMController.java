package UI.application.DmTab.DMencrypt;

import UI.application.AllMachineController;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.DMencrypt.DMoperational.DMoperationalController;
import UI.application.DmTab.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import UI.application.DmTab.Trie.Trie;
import UI.application.DmTab.Trie.TrieNode;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import decryptionManager.DecryptionManager;
import dtoObjects.CodeFormatDTO;
import enigmaEngine.Encryptor;
import enigmaEngine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class encryptTabDMController {

    public TextField searchBox;

    public Button searchButton;
    public Button deleteButton;
    public ListView dictionaryListView;
    public VBox codeEncryptComponent;

    public AutomaticEncryptDMController codeEncryptComponentController;

    @FXML private GridPane operationalComponent;
    @FXML private DMoperationalController operationalComponentController;
    @FXML
    private HBox simpleCodeComponent;

    @FXML
    private SimpleCodeController simpleCodeComponentController;
   private  Trie dictionaryTrie;
    private Encryptor encryptor;
   private DMcontroller DmController;

    private  ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
    private Engine enigmaEngine;
    private SimpleStringProperty outputString;
    public DecryptionManager getDecryptionManager() {
        return operationalComponentController.getDecryptionManager();
    }
    @FXML
    private void initialize() {

        if(codeEncryptComponentController!=null)
        {
            codeEncryptComponentController.setParentComponentTab(this);
        }
        dictionaryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//        dictionaryListView.setOnMouseClicked(new EventHandler<Event>() {
//            @Override
//            public void handle(Event event) {
//                ObservableList<String> selectedItems =  dictionaryListView.getSelectionModel().getSelectedItems();
//                List<String> selectedWordsList = new ArrayList<>(selectedItems);
//                String input = StringUtils.join(selectedWordsList, " ");
//                codeEncryptComponentController.getInputString().setText(input);
//
//            }
//
//        });



        dictionaryListView.getSelectionModel().selectedItemProperty().addListener
                ((ObservableValue ov, Object old_val, Object new_val) -> {
                    ObservableList<String> selectedItems = dictionaryListView.getSelectionModel().getSelectedItems();

                    StringBuilder builder = new StringBuilder();

                    for (String word : selectedItems) {
                        builder.append(word+" ");
                    }

                    codeEncryptComponentController.getInputString().setText(builder.toString());

                });

        outputString=new SimpleStringProperty();
        bindOutputStringBetweenComponent();

        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
    }
    public void bindOutputStringBetweenComponent()
    {
        codeEncryptComponentController.bindParentToOutputString(outputString);
        operationalComponentController.bindOutputStringToParent(outputString);

    }

    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }


    public void doneProcessData()
    {
        simpleCodeComponentController.setSelectedCode(DmController.getEnigmaEngine().getCodeFormat(false));
        DmController.getMainAppController().bindCurrentBFCode();
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

    public AllMachineController getMainController(){
        return DmController.getMainAppController();
    }


    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        codeEncryptComponentController.setEncryptor(enigmaEngine);
        operationalComponentController.setEnigmaEngine(enigmaEngine);
    }

    public SimpleCodeController getCodeComponentController() {
      return simpleCodeComponentController;
    }

    public void setSelectedCode(CodeFormatDTO currentCode) {
        simpleCodeComponentController.setSelectedCode(currentCode);
    }

    public void setDisableBind(SimpleBooleanProperty isSelected) {
        simpleCodeComponent.disableProperty().bind(isSelected.not());
    }

    public void clearListView() {
        dictionaryListView.getSelectionModel().getSelectedItems().removeAll();
    }
}
