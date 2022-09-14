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
import com.sun.deploy.util.StringUtils;
import dtoObjects.CodeFormatDTO;
import enigmaEngine.Encryptor;
import enigmaEngine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class encryptTabDMController {

    public TextField searchBox;

    public Button searchButton;
    public Button deleteButton;
    public ListView dictionaryListView;
    public VBox codeEncryptComponent;

    public AutomaticEncryptDMController codeEncryptComponentController;


    @FXML
    private HBox simpleCodeComponent;

    @FXML
    private SimpleCodeController simpleCodeComponentController;
    Trie dictionaryTrie;
    private Encryptor encryptor;
   private DMcontroller DmController;

    private  ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
    private Engine enigmaEngine;

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



        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
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
