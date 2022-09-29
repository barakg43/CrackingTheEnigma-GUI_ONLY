package UI.application.Uboat.ContestTab;

import UI.application.DmTab.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import UI.application.DmTab.Trie.Trie;
import UI.application.DmTab.Trie.TrieNode;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import enigmaEngine.Engine;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class EncryptController {
    @FXML private  ScrollPane dictionaryScrollPane;
    @FXML private  StackPane dictionaryStackPane;
    @FXML private  Button deleteButton;
    @FXML private  TextField searchBox;
    @FXML private  ListView dictionaryListView;
    @FXML private HBox encryptHBox;

    @FXML private HBox simpleCodeComponent;
    private SimpleCodeController simpleCodeComponentController;


    @FXML private ScrollPane codeEncryptComponent;

    private Trie dictionaryTrie;

    public AutomaticEncryptDMController codeEncryptComponentController;

    private Engine enigmaEngine;
    private SimpleStringProperty outputString;


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
        createDictionaryList();

        outputString=new SimpleStringProperty();
        bindOutputStringBetweenComponent();
        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
    }

    public void setDictionaryTrie() {
        dictionaryTrie=new Trie();
    }

    public Trie getTrieDictionary(){return dictionaryTrie;}

    public void search(String oldVal, String newVal) {
        ObservableList<String> subEntries = FXCollections.observableArrayList();

        if(newVal.isEmpty())
        {
           // DmController.setDictionaryList();
        }
        else{
            TrieNode node = dictionaryTrie.searchNode(newVal);
            if(node!=null)
            {
                dictionaryTrie.wordsFinderTraversal(node,0);
                ArrayList<String> prefixWords= dictionaryTrie.getWordsArray();

                subEntries.addAll(prefixWords);

            }
            dictionaryListView.setItems(subEntries);

        }

    }
    public void bindOutputStringBetweenComponent()
    {
        codeEncryptComponentController.bindParentToOutputString(outputString);

    }
    private void createDictionaryList()
    {
        dictionaryListView.getSelectionModel().selectedItemProperty().addListener
                ((ObservableValue ov, Object old_val, Object new_val) -> {
                    ObservableList<String> selectedItems = dictionaryListView.getSelectionModel().getSelectedItems();

                    StringBuilder builder = new StringBuilder();

                    for(int i=0;i<selectedItems.size()-1;i++)
                        builder.append(selectedItems.get(i)+" ");

                    builder.append(selectedItems.get(selectedItems.size()-1));

                    //    System.out.println(builder.toString());

                    codeEncryptComponentController.getInputString().setText(builder.toString());

                });
    }

    public void deleteButtonOnAction(ActionEvent ignoredActionEvent) {
        searchBox.clear();
    }
}
